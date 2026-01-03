package com.buxxy.buxxy_fraud_engine.dto.fraudscore;

import com.buxxy.buxxy_fraud_engine.enums.Decision;
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
}
