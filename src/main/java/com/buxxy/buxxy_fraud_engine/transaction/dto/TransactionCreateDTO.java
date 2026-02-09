package com.buxxy.buxxy_fraud_engine.transaction.dto;

import java.math.BigDecimal;

import com.buxxy.buxxy_fraud_engine.transaction.status.TransactionStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionCreateDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal transactionAmount;

    @NotBlank
    private String transactionAwayCountry;

    @NotBlank
    private String transactionAwayCity;

    @NotNull
    private TransactionStatus transactionStatus;

    private String ipAddress;


}