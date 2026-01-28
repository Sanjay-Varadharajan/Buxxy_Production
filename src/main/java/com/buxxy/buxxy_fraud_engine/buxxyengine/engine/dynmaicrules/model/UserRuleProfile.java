package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.dynmaicrules.model;

import com.buxxy.buxxy_fraud_engine.model.FraudRules;
import com.buxxy.buxxy_fraud_engine.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRuleProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userRuleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private FraudRules ruleId;

    private BigDecimal avgAmount;

    private Integer txCountPerHour;

    private String usualCountries;

    private Integer deviceCount;

    private LocalDateTime lastTxTime;

    private Integer dynamicThreshold;

    private BigDecimal multiplier;

    private LocalDateTime updatedOn;
}
