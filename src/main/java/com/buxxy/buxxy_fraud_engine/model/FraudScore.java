package com.buxxy.buxxy_fraud_engine.model;

import com.buxxy.buxxy_fraud_engine.enums.Decision;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "fraud_score",
        indexes = {
                @Index(name = "idx_fraud_transaction", columnList = "transaction_id")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FraudScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fraudScoreId;

    @JoinColumn(name = "transactionId",nullable = false,unique = true)
    @OneToOne
    private Transaction transaction;

    private int riskScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Decision decision;
}
