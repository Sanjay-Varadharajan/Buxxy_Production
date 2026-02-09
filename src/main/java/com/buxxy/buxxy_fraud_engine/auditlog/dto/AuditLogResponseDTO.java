package com.buxxy.buxxy_fraud_engine.auditlog.dto;

import com.buxxy.buxxy_fraud_engine.auditlog.auditstatus.AuditStatus;
import com.buxxy.buxxy_fraud_engine.auditlog.model.AuditLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponseDTO {
    private Long logId;
    private Long eUserId;
    private String action;
    private LocalDateTime auditedOn;
    private AuditStatus status;

    public AuditLogResponseDTO(AuditLog auditLog) {
        this.logId= auditLog.getLogId();
        this.eUserId=auditLog.getExternalUser().getEUserId();
        this.status=auditLog.getStatus();
        this.action=auditLog.getAction();
        this.auditedOn=auditLog.getAuditedOn();
    }
}
