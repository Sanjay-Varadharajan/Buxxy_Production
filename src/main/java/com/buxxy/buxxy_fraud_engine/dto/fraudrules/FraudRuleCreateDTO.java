package com.buxxy.buxxy_fraud_engine.dto.fraudrules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.buxxy.buxxy_fraud_engine.enums.RuleType;
import com.buxxy.buxxy_fraud_engine.model.FraudRules;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FraudRuleCreateDTO {
    @NotBlank
    private String ruleDescription;

    private BigDecimal ruleThreshold;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @NotBlank(message = "Rule type must not be blank")
    private RuleType ruleType;

    private LocalDateTime ruleUpdatedOn;

    public FraudRuleCreateDTO(FraudRules fraudRules) {
        this.ruleDescription=fraudRules.getRuleDescription();
        this.ruleThreshold=fraudRules.getThreshold();
        this.ruleUpdatedOn=fraudRules.getRuleUpdatedOn();
    }
}
