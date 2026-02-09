package com.buxxy.buxxy_fraud_engine.transaction.model;


import com.buxxy.buxxy_fraud_engine.externaluser.model.ExternalUser;
import com.buxxy.buxxy_fraud_engine.engine.fraudscore.model.FraudScore;
import com.buxxy.buxxy_fraud_engine.transaction.status.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_transaction_user", columnList = "external_user_id"),
                @Index(name = "idx_transaction_user_time", columnList = "external_user_id, transactionOn"),
                @Index(name = "idx_transaction_status_time", columnList = "transactionStatus, transactionOn")
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(nullable = false)
    private BigDecimal transactionAmount;

    @NotBlank
    @Column(nullable = false)
    private String transactionHomeCountry;

    @NotBlank
    @Column(nullable = false)
    private String transactionHomeCity;

    @NotBlank
    @Column(nullable = false)
    private String transactionAwayCountry;

    @NotBlank
    @Column(nullable = false)
    private String transactionAwayCity;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime transactionOn;

    @JoinColumn(name ="external_user_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JsonIgnore
    private ExternalUser externalUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private FraudScore fraudScore;

    @Column(length = 45)
    private String ipAddress;


}
