package com.buxxy.buxxy_fraud_engine.otp;

import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.model.OTP;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.AuditRepository;
import com.buxxy.buxxy_fraud_engine.repositories.OtpRepository;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    private final TransactionRepository transactionRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuditRepository auditRepository;

    public String generateAndSaveOtp(Transaction transaction, User user){
        int otpInt=100000+secureRandom.nextInt(900000);
        String otpValue=String.valueOf(otpInt);

        OTP otp=new OTP();
        otp.setTransaction(transaction);
        otp.setUser(user);
        otp.setOtpValue(bCryptPasswordEncoder.encode(otpValue));
        otp.setUsed(false);
        otp.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otp);
        return otpValue;
    }

    public boolean validateOtp(long transactionId,String otpValue){
        Optional<OTP> otpOpt = otpRepository.findByTransactionTransactionIdAndOtpValueAndUsedFalse(transactionId, otpValue);

        if (otpOpt.isPresent()){
            OTP otp=otpOpt.get();
            if(bCryptPasswordEncoder.matches(otpValue,otp.getOtpValue()) &&
            otp.getOtpExpiry().isAfter(LocalDateTime.now())){
                otp.setUsed(true);
                otpRepository.save(otp);
                return true;
            }
        }
        return false;

    }

    public String verifyOtp(Long transactionId, String otp) {
        Optional<Transaction> txnOpt = transactionRepository.findById(transactionId);

        if (txnOpt.isEmpty())
            return "Transaction not found";

        Transaction transaction = txnOpt.get();

        if (transaction.getTransactionStatus() != TransactionStatus.PENDING)
            return "Transaction already completed or blocked";

        boolean valid = validateOtp(transactionId, otp);

        if (valid) {
            transaction.setTransactionStatus(TransactionStatus.APPROVED);
            transactionRepository.save(transaction);
            AuditLog auditLog=new AuditLog();
            auditLog.setStatus(AuditStatus.SUCCESS);
            auditLog.setAction("otp Verified And Transaction is Allowed");
            auditLog.setUser(transaction.getUser());
            auditRepository.save(auditLog);
            return "OTP verified. Transaction completed successfully.";
        } else {
            transaction.setTransactionStatus(TransactionStatus.BLOCKED);
            transaction.setTransactionAmount(null);
            transaction.setTransactionLocation(null);
            transactionRepository.save(transaction);
            AuditLog auditLog=new AuditLog();
            auditLog.setUser(transaction.getUser());
            auditLog.setAction("otp verification failed and transaction is blocked");
            auditLog.setStatus(AuditStatus.FAILURE);
            auditRepository.save(auditLog);
            return "Invalid or expired OTP. Transaction blocked.";
        }
    }

}
