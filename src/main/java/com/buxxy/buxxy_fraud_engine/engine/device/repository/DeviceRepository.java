package com.buxxy.buxxy_fraud_engine.engine.device.repository;

import com.buxxy.buxxy_fraud_engine.engine.device.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    Optional<Device> findByUserIdAndFingerprintHash(Long userId, String fingerprintHash);


}
