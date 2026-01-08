package com.buxxy.buxxy_fraud_engine.dto.fraudrules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.buxxy.buxxy_fraud_engine.model.FraudRules;
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

    @NotNull
    private BigDecimal ruleThreshold;

    private LocalDateTime ruleUpdatedOn;

    public FraudRuleCreateDTO(FraudRules fraudRules) {
        this.ruleDescription=fraudRules.getRuleDescription();
        this.ruleThreshold=fraudRules.getThreshold();
        this.ruleUpdatedOn=fraudRules.getRuleUpdatedOn();
    }
}
