package com.buxxy.buxxy_fraud_engine.dto.otp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OTPGenerateDTO {
    @NotNull
    private Long userId;

    @NotNull
    private Long transactionId;
}

