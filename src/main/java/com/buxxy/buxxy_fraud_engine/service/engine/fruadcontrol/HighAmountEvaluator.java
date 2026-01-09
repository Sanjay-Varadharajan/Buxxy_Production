package com.buxxy.buxxy_fraud_engine.service.engine.fruadcontrol;

import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.model.FraudRules;
import com.buxxy.buxxy_fraud_engine.model.Transaction;

public class HighAmountEvaluator implements RuleEvaluator{
    @Override
    public String ruleType() {
        return "HIGH_AMOUNT";
    }

    @Override
    public Decision evaluate(FraudRules fraudRules, Transaction transaction) {
        
    }
}
