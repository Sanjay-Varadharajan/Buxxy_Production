package com.buxxy.buxxy_fraud_engine.repositories;


import com.buxxy.buxxy_fraud_engine.model.SystemApiKeyAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyAttemptRepository extends JpaRepository<SystemApiKeyAttempt,Integer> {
}
