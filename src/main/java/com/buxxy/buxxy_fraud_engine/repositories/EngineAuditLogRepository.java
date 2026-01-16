package com.buxxy.buxxy_fraud_engine.repositories;

import com.buxxy.buxxy_fraud_engine.model.AuditLogForEngine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EngineAuditLogRepository extends JpaRepository<AuditLogForEngine,Long> {
}
