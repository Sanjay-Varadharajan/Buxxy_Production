package com.buxxy.buxxy_fraud_engine.dto.auditlog;

import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import com.buxxy.buxxy_fraud_engine.model.AuditLog;
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
    private Long userId;
    private String action;
    private LocalDateTime auditedOn;
    private AuditStatus status;

    public AuditLogResponseDTO(AuditLog auditLog) {
        this.logId= auditLog.getLogId();
        this.userId=auditLog.getUser().getUserId();
        this.status=auditLog.getStatus();
        this.action=auditLog.getAction();
        this.auditedOn=auditLog.getAuditedOn();
    }
}
