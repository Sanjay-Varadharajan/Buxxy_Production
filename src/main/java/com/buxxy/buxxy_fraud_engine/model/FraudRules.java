package com.buxxy.buxxy_fraud_engine.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "fraud_rules",
        indexes = {
                @Index(name = "idx_rules_threshold", columnList = "threshold"),
                @Index(name = "idx_rules_active", columnList = "inActive")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FraudRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ruleId;

    @NotBlank(message = "description must not be Blank")
    private String ruleDescription;

    @NotNull(message = "Should not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal threshold;

    private boolean inActive;

}