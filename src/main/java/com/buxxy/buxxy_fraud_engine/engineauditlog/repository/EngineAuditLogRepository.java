package com.buxxy.buxxy_fraud_engine.engineauditlog.repository;

import com.buxxy.buxxy_fraud_engine.engineauditlog.model.AuditLogForEngine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngineAuditLogRepository extends JpaRepository<AuditLogForEngine,Long> {
}
