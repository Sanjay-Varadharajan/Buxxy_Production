package com.buxxy.buxxy_fraud_engine.engine.handlingdecision;


import com.buxxy.buxxy_fraud_engine.auditlog.auditstatus.AuditStatus;
import com.buxxy.buxxy_fraud_engine.engine.handlingdecision.decisions.Decision;
import com.buxxy.buxxy_fraud_engine.transaction.status.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.auditlog.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.transaction.model.Transaction;
import com.buxxy.buxxy_fraud_engine.otp.mailservice.EmailService;
import com.buxxy.buxxy_fraud_engine.otp.service.OtpService;
import com.buxxy.buxxy_fraud_engine.auditlog.repository.AuditRepository;
import com.buxxy.buxxy_fraud_engine.transaction.repository.TransactionRepository;
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
        auditLog.setExternalUser(transaction.getExternalUser());
        auditLog.setAction("Transaction Approved");
        auditLog.setStatus(AuditStatus.SUCCESS);
        auditRepository.save(auditLog);

    }

    private void handleStepUp(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);

        String otpValue=otpService.generateAndSaveOtp(transaction,transaction.getExternalUser());

        emailService.sendOtp(transaction.getExternalUser().getEUserMail(),otpValue);
    }

    private void handleBlock(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.BLOCKED);
        transaction.setTransactionAmount(null);

        transactionRepository.save(transaction);

        AuditLog blocked=new AuditLog();
        blocked.setAction("transaction Block due to High Risk");
        blocked.setStatus(AuditStatus.BLOCKED);
        blocked.setExternalUser(transaction.getExternalUser());
        auditRepository.save(blocked);
    }
}
