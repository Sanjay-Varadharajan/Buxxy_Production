package com.buxxy.buxxy_fraud_engine.dto.fraudrules;

import com.buxxy.buxxy_fraud_engine.model.FraudRules;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FraudRuleResponseDTO {
    private Long ruleId;
    private String ruleDescription;
    private BigDecimal threshold;
    private Boolean isActive;
    private LocalDateTime ruleAddedOn;
    private LocalDateTime ruleUpdatedOn;

    public FraudRuleResponseDTO(FraudRules fraudRules) {
        this.ruleId=fraudRules.getRuleId();
        this.ruleDescription=fraudRules.getRuleDescription();
        this.isActive=fraudRules.isActive();
        this.threshold=fraudRules.getThreshold();
        this.ruleAddedOn=fraudRules.getRuleAddedOn();
        this.ruleUpdatedOn=fraudRules.getRuleUpdatedOn();
    }
}
