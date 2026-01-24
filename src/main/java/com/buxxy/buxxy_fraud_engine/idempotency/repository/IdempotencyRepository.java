package com.buxxy.buxxy_fraud_engine.idempotency.repository;

import com.buxxy.buxxy_fraud_engine.idempotency.model.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord,Long> {

    Optional<IdempotencyRecord> findByIdempotencyKey(String idempotencyKey);


}
