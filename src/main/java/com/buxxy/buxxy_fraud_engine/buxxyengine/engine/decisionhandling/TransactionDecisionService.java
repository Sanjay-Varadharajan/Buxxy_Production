package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.decisionhandling;


import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.otp.EmailService;
import com.buxxy.buxxy_fraud_engine.otp.OtpService;
import com.buxxy.buxxy_fraud_engine.repositories.AuditRepository;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class TransactionDecisionService {

    private final TransactionRepository transactionRepository;
    private final AuditRepository auditRepository;
    private final OtpService otpService;
    private final EmailService emailService;


    public Transaction decisionHandling(Decision decision,Transaction transaction
                                                     ) {
        switch (decision) {

            case BLOCK ->handleBlock(transaction);

            case STEP_UP ->handleStepUp(transaction);

            case ALLOW ->handleAllow(transaction);
        }

        return transaction;
    }

    private void handleAllow(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.APPROVED);
        transactionRepository.save(transaction);
        AuditLog auditLog = new AuditLog();
        auditLog.setUser(transaction.getUser());
        auditLog.setAction("Transaction Approved");
        auditLog.setStatus(AuditStatus.SUCCESS);
        auditRepository.save(auditLog);

    }

    private void handleStepUp(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);

        String otpValue=otpService.generateAndSaveOtp(transaction,transaction.getUser());

        emailService.sendOtp(transaction.getUser().getUserMail(),otpValue);
    }

    private void handleBlock(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.BLOCKED);
        transaction.setTransactionAmount(null);

        transactionRepository.save(transaction);

        AuditLog blocked=new AuditLog();
        blocked.setAction("transaction Block due to High Risk");
        blocked.setStatus(AuditStatus.BLOCKED);
        blocked.setUser(transaction.getUser());
        auditRepository.save(blocked);
    }
}
