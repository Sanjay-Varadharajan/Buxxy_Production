package com.buxxy.buxxy_fraud_engine.model;


import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
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
                @Index(name = "idx_transaction_user", columnList = "user_id"),
                @Index(name = "idx_transaction_user_time", columnList = "user_id, transactionOn"),
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

    @NotBlank(message = "Location is Needed")
    @Column(nullable = false)
    private String transactionLocation;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime transactionOn;

    @JoinColumn(name ="user_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus=TransactionStatus.PENDING;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private FraudScore fraudScore;


    @PrePersist
    public void persist() {
        if (transactionOn == null) {
            this.transactionOn = LocalDateTime.now();
        }
    }
}
