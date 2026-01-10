package com.buxxy.buxxy_fraud_engine.otp;


import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
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
        String verify=
                otpService.verifyOtp(transactionId,otp);

        return ResponseEntity.ok(verify);
    }



}
