package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.fruadcontrol;

import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.model.FraudRules;
import com.buxxy.buxxy_fraud_engine.model.Transaction;

public interface RuleEvaluator {

    String ruleType();
    Decision evaluate(FraudRules fraudRules, Transaction transaction);
}
