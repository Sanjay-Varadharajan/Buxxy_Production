package com.buxxy.buxxy_fraud_engine.otp;

import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RequiredArgsConstructor
@RestController
public class OtpController {

    private final TransactionRepository transactionRepository;

    private final  OtpService otpService;


    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam Long transactionId, @RequestParam String otp) {
        Optional<Transaction> txnOpt = transactionRepository.findById(transactionId);

        if (txnOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");

        Transaction transaction = txnOpt.get();

        if (transaction.getTransactionStatus() != TransactionStatus.PENDING)
            return ResponseEntity.badRequest().body("Transaction already completed or blocked");

        boolean valid = otpService.validateOtp(transactionId, otp);

        if (valid) {
            transaction.setTransactionStatus(TransactionStatus.APPROVED);
            transactionRepository.save(transaction);
            return ResponseEntity.ok("OTP verified. Transaction completed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
        }
    }



}
