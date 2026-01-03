package com.buxxy.buxxy_fraud_engine.dto.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OTPResponseDTO {
    private Long otpId;
    private Long userId;
    private Long transactionId;
    private String otpValue;
    private LocalDateTime expiry;
}
