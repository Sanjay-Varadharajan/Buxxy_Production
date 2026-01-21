package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.device.service;


import com.buxxy.buxxy_fraud_engine.enums.DeviceEvent;
import com.buxxy.buxxy_fraud_engine.model.Device;
import com.buxxy.buxxy_fraud_engine.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetectionService {


    private final DeviceRepository deviceRepository;

    private final DeviceFingerPrintService deviceFingerPrintService;

    public DeviceEvent detectDevice(long userId, String userAgent, String timeZone, String language) {


        String fingerprintHash = deviceFingerPrintService.fingerprint(userAgent, timeZone, language);
        String userAgentHash = deviceFingerPrintService.sha256(fingerprintHash);

        Optional<Device> existingDevice = deviceRepository
                .findByUserIdAndFingerprintHash(userId, userAgentHash);

        if (existingDevice.isEmpty()) {
            Device device = new Device();
            device.setFingerprintHash(fingerprintHash);
            device.setFirstSeen(Instant.now());
            device.setLastSeen(Instant.now());
            device.setUserId(userId);
            device.setUserAgentHash(userAgentHash);
            device.setLanguage(language);
            device.setTimeZone(timeZone);

            deviceRepository.save(device);
            return DeviceEvent.NEW_DEVICE;
        }
        Device device = existingDevice.get();

        boolean tampered = !device.getUserAgentHash().equals(userAgentHash) ||
                !device.getFingerprintHash().equals(fingerprintHash) ||
                !device.getLanguage().equals(language);

        device.setLastSeen(Instant.now());
        deviceRepository.save(device);

        if (tampered) {
            return DeviceEvent.DEVICE_MISMATCH;
        }

        return DeviceEvent.KNOWN_DEVICE;

    }
    public Device detectAndGetDevice(long userId,String userAgent,String timeZone,String acceptedLanguage){
        String fingerPrintHash=deviceFingerPrintService.fingerprint(userAgent,timeZone,acceptedLanguage);
        String userAgentHash=deviceFingerPrintService.sha256(fingerPrintHash);

        Optional<Device> existingDevice=deviceRepository.findByUserIdAndFingerprintHash(userId,fingerPrintHash);

        if(existingDevice.isPresent()){
            Device device=existingDevice.get();
            boolean deviceTampered=!device.getUserAgentHash().equals(userAgentHash)||
                    !device.getFingerprintHash().equals(fingerPrintHash)||
                    !device.getLanguage().equals(acceptedLanguage);
            device.setLastSeen(Instant.now());
            deviceRepository.save(device);
            return device;
        }
        Device device=new Device();
        device.setLastSeen(Instant.now());
        device.setFirstSeen(Instant.now());
        device.setLanguage(acceptedLanguage);
        device.setTimeZone(timeZone);
        device.setUserId(userId);
        device.setFingerprintHash(fingerPrintHash);
        device.setUserAgentHash(userAgentHash);

        deviceRepository.save(device);
        return device;
    }
}
