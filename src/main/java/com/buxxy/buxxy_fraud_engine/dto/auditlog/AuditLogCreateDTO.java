package com.buxxy.buxxy_fraud_engine.dto.auditlog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditLogCreateDTO {
    @NotNull
    private Long userId;

    @NotBlank
    private String action;

    private AuditStatus status;
}
