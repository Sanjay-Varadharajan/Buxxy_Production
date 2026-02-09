package com.buxxy.buxxy_fraud_engine.otp.controller;


import com.buxxy.buxxy_fraud_engine.otp.service.OtpService;
import com.buxxy.buxxy_fraud_engine.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class OtpController {

    private final TransactionRepository transactionRepository;

    private final OtpService otpService;


    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam Long transactionId, @RequestParam String otp) {
        String verify=
                otpService.verifyOtp(transactionId,otp);

        return ResponseEntity.ok(verify);
    }



}
