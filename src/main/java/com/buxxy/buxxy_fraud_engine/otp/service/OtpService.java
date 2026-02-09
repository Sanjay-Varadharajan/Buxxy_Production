package com.buxxy.buxxy_fraud_engine.otp.service;

import com.buxxy.buxxy_fraud_engine.auditlog.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.auditlog.auditstatus.AuditStatus;
import com.buxxy.buxxy_fraud_engine.engineauditlog.model.AuditLogForEngine;
import com.buxxy.buxxy_fraud_engine.engine.handlingdecision.decisions.Decision;
import com.buxxy.buxxy_fraud_engine.externaluser.model.ExternalUser;
import com.buxxy.buxxy_fraud_engine.otp.model.OTP;
import com.buxxy.buxxy_fraud_engine.transaction.status.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.auditlog.repository.AuditRepository;
import com.buxxy.buxxy_fraud_engine.engineauditlog.repository.EngineAuditLogRepository;
import com.buxxy.buxxy_fraud_engine.otp.repository.OtpRepository;
import com.buxxy.buxxy_fraud_engine.transaction.model.Transaction;
import com.buxxy.buxxy_fraud_engine.transaction.repository.TransactionRepository;
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

    private final EngineAuditLogRepository auditLogForEngineRepository;

    public String generateAndSaveOtp(Transaction transaction, ExternalUser externalUser){
        int otpInt=100000+secureRandom.nextInt(900000);
        String otpValue=String.valueOf(otpInt);

        OTP otp=new OTP();
        otp.setTransaction(transaction);
        otp.setExternalUser(externalUser);
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
        return valid ? handleOtpSuccess(transaction) : handleOtpFailure(transaction);
        }

    private String handleOtpSuccess(Transaction txn) {
        txn.setTransactionStatus(TransactionStatus.APPROVED);
        transactionRepository.save(txn);

        logAudit(txn ,"OTP verified. Transaction allowed.", AuditStatus.SUCCESS, TransactionStatus.APPROVED, Decision.ALLOW);

        return "OTP verified. Transaction completed successfully.";
    }

    private String handleOtpFailure(Transaction txn) {
        txn.setTransactionStatus(TransactionStatus.BLOCKED);
        txn.setTransactionAmount(null);
        transactionRepository.save(txn);

        logAudit(txn, "OTP verification failed. Transaction blocked.", AuditStatus.FAILURE, TransactionStatus.BLOCKED, Decision.BLOCK);

        return "Invalid or expired OTP. Transaction blocked.";
    }

    private void logAudit(Transaction txn, String action, AuditStatus status, TransactionStatus txnStatus, Decision decision) {
        AuditLog auditLog = new AuditLog();
        auditLog.setExternalUser(txn.getExternalUser());
        auditLog.setAction(action);
        auditLog.setStatus(status);
        auditRepository.save(auditLog);

        AuditLogForEngine auditLogForEngine = new AuditLogForEngine();
        auditLogForEngine.setTransaction(txn);
        auditLogForEngine.setExternalUser(txn.getExternalUser());
        auditLogForEngine.setStatus(txnStatus);
        auditLogForEngine.setDecision(decision);
        auditLog.setAuditedOn(LocalDateTime.now());
        auditLogForEngineRepository.save(auditLogForEngine);
    }
    }

