package com.buxxy.buxxy_fraud_engine.engine.fraudscore.repository;

import com.buxxy.buxxy_fraud_engine.engine.fraudscore.model.FraudScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudScoreRepository extends JpaRepository<FraudScore,Long> {
}
