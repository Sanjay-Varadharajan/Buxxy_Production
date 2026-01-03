package com.buxxy.buxxy_fraud_engine.dto.transaction;

import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionResponseDTO {
    private Long transactionId;
    private Long userId;
    private BigDecimal amount;
    private String location;
    private LocalDateTime timestamp;
    private TransactionStatus status;
}