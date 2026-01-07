package com.buxxy.buxxy_fraud_engine.dto.transaction;

import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
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
    private BigDecimal transactionAmount;
    private String transactionLocation;
    private LocalDateTime transactionOn;
    private TransactionStatus transactionStatus;



    public TransactionResponseDTO(Transaction transactionHistory) {
        this.transactionId=transactionHistory.getTransactionId();
        this.userId=transactionHistory.getUser().getUserId();
        this.transactionAmount=transactionHistory.getTransactionAmount();
        this.transactionLocation=transactionHistory.getTransactionLocation();
        this.transactionOn=transactionHistory.getTransactionOn();
        this.transactionStatus=transactionHistory.getTransactionStatus();
    }


}