package com.buxxy.buxxy_fraud_engine.engine.fraudrules.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FraudRuleUpdateDTO {
    @NotNull
    private Long ruleId;

    private String description;
    private BigDecimal threshold;
    private Boolean active;
}
