package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.dynmaicrules.repository;

import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.dynmaicrules.model.UserRuleProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRuleProfileRepository extends JpaRepository<UserRuleProfile,Integer> {
    Optional<UserRuleProfile> findByUserUserIdAndFraudRulesRuleId(long userId,long ruleId);
}
