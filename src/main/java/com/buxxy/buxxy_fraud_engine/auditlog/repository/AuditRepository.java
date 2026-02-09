package com.buxxy.buxxy_fraud_engine.auditlog.repository;

import com.buxxy.buxxy_fraud_engine.auditlog.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog,Long> {
}
