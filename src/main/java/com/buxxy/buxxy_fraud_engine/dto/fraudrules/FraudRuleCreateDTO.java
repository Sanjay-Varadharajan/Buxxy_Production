package com.buxxy.buxxy_fraud_engine.dto.fraudrules;

import java.math.BigDecimal;
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
    private String description;

    @NotNull
    private BigDecimal threshold;

    private Boolean active;
}
