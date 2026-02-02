package com.buxxy.buxxy_fraud_engine.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemApiKeyAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long attemptId;

    private String attemptedKey;

    private String ipAddress;

    private String endpoint;

    private LocalDateTime AttemptedAt;

    private boolean success;
}
