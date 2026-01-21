package com.buxxy.buxxy_fraud_engine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deviceId;

    private Long userId;

    private String fingerprintHash;

    private String userAgentHash;

    private String timeZone;

    private String language;

    private Instant firstSeen;

    private Instant lastSeen;
}
