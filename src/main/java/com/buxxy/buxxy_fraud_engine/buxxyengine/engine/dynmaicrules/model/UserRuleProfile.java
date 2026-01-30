package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.dynmaicrules.model;


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

    @JoinColumn(name = "user_id")
    private long userId;

    @JoinColumn(name = "rule_id")
    private long ruleId;

    private BigDecimal avgAmount;

    private Integer txCountPerHour;


    private Integer deviceCount;

    private LocalDateTime lastTxTime;

    private Integer dynamicThreshold;

    private BigDecimal multiplier;

    private LocalDateTime updatedOn;

    private String homeCountry;

    private String homeCity;

    private List<String> usualTransactionCountries;

    private List<String> usualTransactionCities;

}
