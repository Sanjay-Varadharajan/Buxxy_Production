package com.buxxy.buxxy_fraud_engine.engine.dynmaicrules.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

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

    @Column(name = "user_id")
    private long eUserId;

    @Column(name = "rule_id")
    private long ruleId;

    private BigDecimal avgAmount;

    private Integer txCountPerHour;

    private Integer deviceCount;

    private LocalDateTime lastTxTime;

    private Integer dynamicThreshold;

    private BigDecimal multiplier;

    private LocalDateTime updatedOn;

    @ElementCollection
    private List<String> homeCountries;

    @ElementCollection
    private List<String> homeCities;

    @ElementCollection
    private List<String> awayCountries;

    @ElementCollection
    private List<String> awayCities;

    @ElementCollection
    private List<String> usualTransactionCountries;

    @ElementCollection
    private List<String> usualTransactionCities;
}
