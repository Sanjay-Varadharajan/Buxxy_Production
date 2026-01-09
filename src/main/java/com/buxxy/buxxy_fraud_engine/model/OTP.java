package com.buxxy.buxxy_fraud_engine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "otp",
        indexes = {
                @Index(name="idx_otp_transaction_user", columnList="transaction_id, user_id"),
                @Index(name="idx_otp_expiry", columnList="otpExpiry")
        }
)@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long otpId;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "transaction_id",nullable = false)
    private Transaction transaction;

    private String otpValue;

    @Column(nullable = false)
    private boolean used;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;


}
