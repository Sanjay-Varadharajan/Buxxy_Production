package com.buxxy.buxxy_fraud_engine.otp;

import com.buxxy.buxxy_fraud_engine.model.OTP;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();


    public String generateAndSaveOtp(Transaction transaction, User user){
        int otpInt=100000+secureRandom.nextInt(900000);
        String otpValue=String.valueOf(otpInt);

        OTP otp=new OTP();
        otp.setTransaction(transaction);
        otp.setUser(user);
        otp.setOtpValue(otpValue);
        otp.setUsed(false);
        otp.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otp);
        return otpValue;
    }

    public boolean validateOtp(long transactionId,String otpValue){
        Optional<OTP> otpOpt = otpRepository.findByTransactionTransactionIdAndOtpValueAndUsedFalse(transactionId, otpValue);

        if (otpOpt.isPresent()){
            OTP otp=otpOpt.get();
            if(otp.getOtpExpiry().isAfter(LocalDateTime.now())){
                otp.setUsed(true);
                otpRepository.save(otp);
                return true;
            }
        }
        return false;

    }
}
