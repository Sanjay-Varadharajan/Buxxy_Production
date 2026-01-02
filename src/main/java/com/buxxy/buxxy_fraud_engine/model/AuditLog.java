package com.buxxy.buxxy_fraud_engine.model;

import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Action is Required")
    private String action;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime auditedOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AuditStatus status;


}
