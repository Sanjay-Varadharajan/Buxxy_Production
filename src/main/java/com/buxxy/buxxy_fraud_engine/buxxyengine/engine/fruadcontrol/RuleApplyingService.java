package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.fruadcontrol;

import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.device.service.DetectionService;
import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.extractor.DeviceContextExtractor;
import com.buxxy.buxxy_fraud_engine.dto.fraudrules.FraudRuleDtoForEngine;
import com.buxxy.buxxy_fraud_engine.enums.DeviceEvent;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
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


    private final DetectionService detectionService;



    public boolean ruleApplies(Transaction transaction,
                                List<Transaction> last5,
                                FraudRuleDtoForEngine fraudRule,
                                BigDecimal avgAmount,
                                HttpServletRequest httpServletRequest){

        switch (fraudRule.getRuleType()){
            case HIGH_AMOUNT:
                if(fraudRule.getThreshold()!=null){
                    boolean threshold=transaction.getTransactionAmount().compareTo(fraudRule.getThreshold())>0;
                    return threshold;
                }
                break;

            case VELOCITY:
                if(fraudRule.getMetadata()==null){
                    return false;
                }
                try{
                    JSONObject jsonObject=new JSONObject(fraudRule.getMetadata());
                    int maxCount=jsonObject.optInt("maxCount",0);
                    int windowSeconds=jsonObject.optInt("windowSeconds",0);

                    if(maxCount<=0 || windowSeconds<=0){
                        return false;
                    }
                    long now=transaction.getTransactionOn().toEpochSecond(ZoneOffset.UTC);

                    long count=last5.stream().filter(tx->{
                                long txTime=tx.getTransactionOn().toEpochSecond(ZoneOffset.UTC);

                                return (now-txTime)<=windowSeconds;
                            })
                            .count();

                    return count>=maxCount;
                }catch (Exception e){
                    return false;
                }


            case LOCATION:
                boolean validLocation=last5.stream()
                        .map(Transaction::getTransactionLocation)
                        .filter(loc->loc!=null)
                        .noneMatch(loc->loc.equalsIgnoreCase(transaction.getTransactionLocation()));
                return validLocation;

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
                        transaction.getUser().getUserId(),
                        userAgent,
                        timeZone,
                        language
                );
                return deviceEvent==DeviceEvent.NEW_DEVICE || deviceEvent==DeviceEvent.DEVICE_MISMATCH;
            case IP_CHANGE:


            default:
                return false;
        }
        return false;
    }



}
