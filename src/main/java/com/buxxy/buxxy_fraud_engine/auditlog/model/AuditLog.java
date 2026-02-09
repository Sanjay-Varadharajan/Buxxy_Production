package com.buxxy.buxxy_fraud_engine.auditlog.model;

import com.buxxy.buxxy_fraud_engine.admin.model.Admin;
import com.buxxy.buxxy_fraud_engine.auditlog.auditstatus.AuditStatus;
import com.buxxy.buxxy_fraud_engine.externaluser.model.ExternalUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "audit",
        indexes = {
                @Index(name = "idx_audit_user_time", columnList = "user_id, auditedOn"),
                @Index(name = "idx_audit_status_time", columnList = "status, auditedOn")
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "external_user_id", nullable = false)
    private ExternalUser externalUser;

    @NotBlank(message = "Action is Required")
    private String action;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime auditedOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AuditStatus status;


}
