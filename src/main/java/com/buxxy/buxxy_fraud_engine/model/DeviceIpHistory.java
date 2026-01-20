package com.buxxy.buxxy_fraud_engine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceIpHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ipHistoryId;

    private long userId;

    private Integer deviceId;

    private String ipAddress;

    private String ipCountry;

    private String ipState;

    private String ipCity;

    private Instant seenAt;

}
