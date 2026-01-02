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
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "transactionId",nullable = false)
    private Transaction transaction;

    private String otpValue;

    @Column(nullable = false)
    private boolean used;


    private LocalDateTime otpExpiry;


}
