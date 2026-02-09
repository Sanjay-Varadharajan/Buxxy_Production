package com.buxxy.buxxy_fraud_engine.engineauditlog.model;

import com.buxxy.buxxy_fraud_engine.engine.handlingdecision.decisions.Decision;
import com.buxxy.buxxy_fraud_engine.externaluser.model.ExternalUser;
import com.buxxy.buxxy_fraud_engine.transaction.status.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.transaction.model.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AuditLogForEngine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long auditLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ExternalUser externalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    private Decision decision;

    private TransactionStatus status;

    private LocalDateTime auditCreatedAt;



}
