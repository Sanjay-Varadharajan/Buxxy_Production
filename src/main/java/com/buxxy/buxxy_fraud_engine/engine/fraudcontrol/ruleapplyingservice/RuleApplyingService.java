package com.buxxy.buxxy_fraud_engine.engine.fraudcontrol.ruleapplyingservice;

import com.buxxy.buxxy_fraud_engine.engine.device.service.DetectionService;
import com.buxxy.buxxy_fraud_engine.engine.dynmaicrules.model.UserRuleProfile;
import com.buxxy.buxxy_fraud_engine.engine.dynmaicrules.service.UserProfileService;
import com.buxxy.buxxy_fraud_engine.engine.device.extractor.DeviceContextExtractor;
import com.buxxy.buxxy_fraud_engine.engine.location.geolocation.service.GeoLocationService;
import com.buxxy.buxxy_fraud_engine.engine.ip.IpServiceForAnomaly;
import com.buxxy.buxxy_fraud_engine.engine.location.calculatedistance.CalculateDistance;
import com.buxxy.buxxy_fraud_engine.engine.fraudrules.dto.FraudRuleDtoForEngine;
import com.buxxy.buxxy_fraud_engine.engine.device.deviceevent.DeviceEvent;
import com.buxxy.buxxy_fraud_engine.engine.device.model.Device;
import com.buxxy.buxxy_fraud_engine.engine.device.deviceIphistory.model.DeviceIpHistory;
import com.buxxy.buxxy_fraud_engine.transaction.model.Transaction;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RuleApplyingService {

    private final DeviceContextExtractor deviceContextExtractor;


    private final IpServiceForAnomaly ipServiceForAnomaly;

    private final DetectionService detectionService;

    private final UserProfileService userProfileService;

    private final GeoLocationService geoLocationService;

    private final CalculateDistance calculateDistance;


    public boolean ruleApplies(Transaction transaction,
                                List<Transaction> last5,
                                FraudRuleDtoForEngine fraudRule,
                                BigDecimal avgAmount,
                                HttpServletRequest httpServletRequest){

        switch (fraudRule.getRuleType()) {
            case HIGH_AMOUNT:
                if (fraudRule.getThreshold() != null) {
                    UserRuleProfile profile = userProfileService.getOrCreateProfile(transaction.getExternalUser(), fraudRule);

                    int windowSize = 5;
                    BigDecimal multiplier = profile.getMultiplier() != null ? profile.getMultiplier() : BigDecimal.valueOf(3);
                    BigDecimal minThreshold = BigDecimal.valueOf(10);

                    if (fraudRule.getMetadata() != null && !fraudRule.getMetadata().isEmpty()) {
                        JSONObject json = new JSONObject(fraudRule.getMetadata());
                        windowSize = json.optInt("windowSize", windowSize);
                        multiplier = BigDecimal.valueOf(json.optDouble("multiplier", multiplier.doubleValue()));
                        if (multiplier.compareTo(profile.getMultiplier()) != 0) {
                            profile.setMultiplier(multiplier);
                        }
                    }
                    BigDecimal txAmount = transaction.getTransactionAmount();
                    BigDecimal dynamicThreshold = profile.getAvgAmount().multiply(multiplier);
                    if (dynamicThreshold.compareTo(minThreshold) < 0) {
                        dynamicThreshold = minThreshold;
                    }
                    boolean isSuspicious = txAmount.compareTo(dynamicThreshold) > 0;

                    userProfileService.updatedAverageAmount(profile, txAmount, windowSize);
                    return isSuspicious;
                }
                break;

            case VELOCITY:
                UserRuleProfile userRuleProfile = userProfileService.getOrCreateProfile(transaction.getExternalUser(), fraudRule);

                long now = transaction.getTransactionOn().toEpochSecond(ZoneOffset.UTC);

                long count = last5.stream()
                        .filter(tx -> {
                            long txTime = tx.getTransactionOn().toEpochSecond(ZoneOffset.UTC);
                            return (now - txTime) <= 3600; // could replace 3600 with a dynamic window
                        })
                        .count();

                boolean isFraud = count >= userRuleProfile.getDynamicThreshold();

                userProfileService.updatedTxCount(userRuleProfile, (int) count);
                userProfileService.updatedAverageAmount(userRuleProfile, transaction.getTransactionAmount(), last5.size() + 1);

                if (fraudRule.getMetadata() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(fraudRule.getMetadata());
                        int maxCount = jsonObject.optInt("maxCount", 0);
                        int windowSeconds = jsonObject.optInt("windowSeconds", 0);

                        if (maxCount > 0 && windowSeconds > 0) {
                            long countStatic = last5.stream()
                                    .filter(tx -> {
                                        long txTime = tx.getTransactionOn().toEpochSecond(ZoneOffset.UTC);
                                        return (now - txTime) <= windowSeconds;
                                    }).count();

                            isFraud = isFraud || countStatic >= maxCount;
                        }
                    } catch (Exception ignored) {
                        System.err.print(ignored);
                    }
                }
                userProfileService.updatedTxCount(userRuleProfile, (int) count);
                userProfileService.updatedAverageAmount(userRuleProfile, transaction.getTransactionAmount(), last5.size() + 1);
                return isFraud;

            case LOCATION:
                UserRuleProfile profile = userProfileService.getOrCreateProfile(transaction.getExternalUser(), fraudRule);


                String homeCountry = geoLocationService.getCountry(transaction.getIpAddress());
                String homeCity = geoLocationService.getCity(transaction.getIpAddress());
                double[] currentLatLon = geoLocationService.getLatitudeAndLongitude(transaction.getIpAddress());


                boolean unusualHomeCountry = profile.getUsualTransactionCountries() == null ||
                        profile.getUsualTransactionCountries()
                                .stream().noneMatch(c -> c.equalsIgnoreCase(homeCountry));

                boolean unusualHomeCity = profile.getUsualTransactionCities() == null ||
                        profile.getUsualTransactionCities()
                                .stream().noneMatch(ci -> ci.equalsIgnoreCase(homeCity));

                boolean unusualAwayCountry = profile.getAwayCountries() == null ||
                        profile.getAwayCountries().stream().noneMatch(c -> c.equalsIgnoreCase(homeCountry));
                boolean unusualAwayCity = profile.getAwayCities() == null ||
                        profile.getAwayCities().stream().noneMatch(c -> c.equalsIgnoreCase(homeCity));

                boolean newCity = last5.stream()
                        .map(tx -> geoLocationService.getCity(tx.getIpAddress()))
                        .filter(loc -> loc != null)
                        .noneMatch(loc -> loc.equalsIgnoreCase(homeCity));

                boolean impossibleTravel = false;

                if (!last5.isEmpty()) {
                    Transaction lastTnx = last5.get(last5.size() - 1);
                    double[] lastLatLon = geoLocationService.getLatitudeAndLongitude(lastTnx.getIpAddress());
                    if (lastLatLon != null && currentLatLon != null) {
                        long secondDiff = java.time.Duration.between(lastTnx.getTransactionOn(), transaction.getTransactionOn()).getSeconds();
                        if (secondDiff > 0) {
                            double distanceKm = calculateDistance.distanceKm(
                                    lastLatLon[0], lastLatLon[1],
                                    currentLatLon[0], currentLatLon[1]
                            );
                            double speedKmh = distanceKm / (secondDiff / 3600.0);
                            impossibleTravel = speedKmh > 1000;
                        }
                    }
                }
                    userProfileService.updateUsual(profile, homeCountry, homeCity);

                    return unusualAwayCity || unusualAwayCountry || unusualHomeCity || unusualHomeCountry || newCity || impossibleTravel;


            case TIME_WINDOW:
                try {
                    List<Transaction> recentTx = last5.stream()
                            .filter(tx -> !tx.getTransactionOn().isAfter(transaction.getTransactionOn()))
                            .toList();

                    if (recentTx.isEmpty()) return false;

                    long nowEpoch = transaction.getTransactionOn().toEpochSecond(ZoneOffset.UTC);

                    if (fraudRule.getMetadata() != null && !fraudRule.getMetadata().isEmpty()) {
                        JSONObject json = new JSONObject(fraudRule.getMetadata());
                        int windowSeconds = json.optInt("windowSeconds", -1);
                        int score = json.optInt("score", 10);

                        if (windowSeconds > 0) {
                            long avgEpoch = (long) recentTx.stream()
                                    .mapToLong(tx -> tx.getTransactionOn().toEpochSecond(ZoneOffset.UTC))
                                    .average()
                                    .orElse(nowEpoch);

                            boolean inWindow = nowEpoch >= (avgEpoch - windowSeconds) &&
                                    nowEpoch <= (avgEpoch + windowSeconds);

                            return !inWindow;
                        }
                    }

                    double[] epochs = recentTx.stream()
                            .mapToDouble(tx -> tx.getTransactionOn().toEpochSecond(ZoneOffset.UTC))
                            .toArray();

                    double avgEpoch = (long) java.util.Arrays.stream(epochs).average().orElse(nowEpoch);
                    double variance = java.util.Arrays.stream(epochs)
                            .map(epoch -> Math.pow(epoch - avgEpoch, 2))
                            .average()
                            .orElse(0);
                    double stdDev = (long) Math.sqrt(variance);

                    double effectiveWindow = Math.max(stdDev, 60);
                    boolean isInsideWindow = nowEpoch >= (avgEpoch - effectiveWindow) &&
                            nowEpoch <= (avgEpoch + effectiveWindow);
                    return !isInsideWindow;

                } catch (Exception e) {
                    return false;
                }

            case DEVICE_CHANGE:

                if(httpServletRequest==null){
                    return false;
                }

                String userAgent=deviceContextExtractor.getUserAgent(httpServletRequest);
                String timeZone=deviceContextExtractor.getTimeZone(httpServletRequest);
                String language=deviceContextExtractor.getLanguage(httpServletRequest);

                DeviceEvent deviceEvent=   detectionService.detectDevice(
                        transaction.getExternalUser().getEUserId(),
                        userAgent,
                        timeZone,
                        language
                );
                return deviceEvent==DeviceEvent.NEW_DEVICE || deviceEvent==DeviceEvent.DEVICE_MISMATCH;
            case IP_CHANGE:

                String userAgent1=deviceContextExtractor.getUserAgent(httpServletRequest);
                String timeZone1=deviceContextExtractor.getTimeZone(httpServletRequest);
                String language1=deviceContextExtractor.getLanguage(httpServletRequest);

                Device device = detectionService.detectAndGetDevice(
                        transaction.getExternalUser().getEUserId(),
                        userAgent1,
                        timeZone1,
                        language1
                );

                DeviceIpHistory deviceIpHistory=ipServiceForAnomaly.checkIpAnomaly(
                        transaction.getExternalUser().getEUserId(),device.getDeviceId(),httpServletRequest);

                return deviceIpHistory.isAnomaly();

            default:
                return false;
        }
        return false;
    }
}
