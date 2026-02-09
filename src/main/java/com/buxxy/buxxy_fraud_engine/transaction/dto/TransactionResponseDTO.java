package com.buxxy.buxxy_fraud_engine.transaction.dto;

import com.buxxy.buxxy_fraud_engine.transaction.status.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.transaction.model.Transaction;
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
    private Long eUserId;
    private BigDecimal transactionAmount;
    private String transactionHomeCountry;
    private String transactionHomeCity;
    private String transactionAwayCountry;
    private String transactionAwayCity;
    private LocalDateTime transactionOn;
    private TransactionStatus transactionStatus;



    public TransactionResponseDTO(Transaction transactionHistory) {
        this.transactionId=transactionHistory.getTransactionId();
        this.eUserId=transactionHistory.getExternalUser().getEUserId();
        this.transactionAmount=transactionHistory.getTransactionAmount();
        this.setTransactionHomeCountry(transactionHistory.getTransactionHomeCountry());
        this.setTransactionHomeCity(transactionHistory.getTransactionHomeCity());
        this.setTransactionAwayCountry(transactionHistory.getTransactionAwayCountry());
        this.setTransactionAwayCity(transactionHistory.getTransactionAwayCity());
        this.transactionOn=transactionHistory.getTransactionOn();
        this.transactionStatus=transactionHistory.getTransactionStatus();
    }


    public TransactionResponseDTO(String status, Long eUserId, String message) {
        this.transactionId = null;
        this.eUserId = eUserId;
        this.transactionAmount = null;
        this.transactionAwayCountry=null;
        this.transactionAwayCity=null;
        this.transactionHomeCity=null;
        this.transactionHomeCountry=null;
        this.transactionOn = null;
        this.transactionStatus = status.equalsIgnoreCase("FAILED") ? TransactionStatus.BLOCKED : null;
    }

}