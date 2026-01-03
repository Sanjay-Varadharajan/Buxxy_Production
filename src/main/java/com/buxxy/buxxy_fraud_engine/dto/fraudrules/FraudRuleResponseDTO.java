package com.buxxy.buxxy_fraud_engine.dto.fraudrules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FraudRuleResponseDTO {
    private Long ruleId;
    private String description;
    private BigDecimal threshold;
    private Boolean active;
}
