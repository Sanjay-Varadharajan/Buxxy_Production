package com.buxxy.buxxy_fraud_engine.dto.auditlog;

import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
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
    private LocalDateTime timestamp;
    private AuditStatus status;
}
