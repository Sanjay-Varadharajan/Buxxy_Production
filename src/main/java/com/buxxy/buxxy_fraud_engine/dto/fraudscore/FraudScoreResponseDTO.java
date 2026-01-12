package com.buxxy.buxxy_fraud_engine.dto.fraudscore;

import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.model.FraudScore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FraudScoreResponseDTO {
    private Long fraudScoreId;
    private Long transactionId;
    private int riskScore;
    private Decision decision;

    public FraudScoreResponseDTO(FraudScore fraudScore) {
        this.fraudScoreId=fraudScore.getFraudScoreId();
        this.transactionId=fraudScore.getTransaction().getTransactionId();
        this.riskScore=fraudScore.getRiskScore();
        this.decision=fraudScore.getDecision();
    }
}
