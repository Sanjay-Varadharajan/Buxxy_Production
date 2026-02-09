package com.buxxy.buxxy_fraud_engine.engine.fraudscore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FraudCheckRequestDTO {
    @NotNull
    private Long transactionId;
}

