package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.ipService;

import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.device.service.DeviceFingerPrintService;
import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.geolocation.GeoLocationService;
import com.buxxy.buxxy_fraud_engine.model.DeviceIpHistory;
import com.buxxy.buxxy_fraud_engine.repositories.DeviceIPHistoryRepository;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IpServiceForAnomaly {

    private final DeviceFingerPrintService deviceFingerPrintService;

    private final TransactionRepository transactionRepository;

    private final DeviceIPHistoryRepository deviceIPHistoryRepository;

    private final GeoLocationService geoLocationService;

    public DeviceIpHistory checkIpAnomaly(
            long userId, long deviceId, HttpServletRequest httpServletRequest){

        String currentIp=httpServletRequest.getRemoteAddr();
        String currentCountry= geoLocationService.getCountry(currentIp);
        String currentCity=geoLocationService.getCity(currentIp);

        Optional<DeviceIpHistory> lastHistory=deviceIPHistoryRepository.findTopByUserIdOrderBySeenAtDesc(userId);

        boolean isAnomaly= Boolean.TRUE.equals(lastHistory.map(last -> {
                    boolean countryChanged = !last.getIpCountry().equalsIgnoreCase(currentCountry);
                    boolean cityChanged=!last.getIpCity().equalsIgnoreCase(currentCity);
                    boolean deviceChanged = !last.getDeviceId().equals(deviceId);
                    return countryChanged || cityChanged || deviceChanged;
                })
                .orElse(false));

        String deviceFingerPrint=deviceFingerPrintService.generateFromRequest(httpServletRequest);

        DeviceIpHistory deviceIpHistory=new DeviceIpHistory();
        deviceIpHistory.setUserId(userId);
        deviceIpHistory.setIpAddress(currentIp);
        deviceIpHistory.setIpCity(currentCity);
        deviceIpHistory.setDeviceId(deviceId);
        deviceIpHistory.setIpCountry(currentCountry);
        deviceIpHistory.setSeenAt(Instant.now());
        deviceIpHistory.setAnomaly(isAnomaly);
        deviceIpHistory.setDeviceFingerPrint(deviceFingerPrint);
        return deviceIPHistoryRepository.save(deviceIpHistory);


    }
}
