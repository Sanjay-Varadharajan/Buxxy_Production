package com.buxxy.buxxy_fraud_engine.repositories;

import com.buxxy.buxxy_fraud_engine.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP,Long> {
    Optional<OTP> findByTransactionTransactionIdAndOtpValueAndUsedFalse(Long transactionId, String otpValue);

}
