package com.buxxy.buxxy_fraud_engine.model;

import com.buxxy.buxxy_fraud_engine.enums.RuleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@EntityListeners(AuditingEntityListener.class)
public class FraudRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ruleId;

    @NotBlank(message = "description must not be Blank")
    private String ruleDescription;

    @NotNull(message = "Should not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal threshold;

   @Enumerated(EnumType.STRING)
    private RuleType ruleType;

    private boolean isActive=true;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime ruleAddedOn;

    private LocalDateTime ruleUpdatedOn;

}