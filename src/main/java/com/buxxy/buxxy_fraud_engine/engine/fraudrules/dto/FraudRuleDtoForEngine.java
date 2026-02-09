    package com.buxxy.buxxy_fraud_engine.engine.fraudrules.dto;

    import com.buxxy.buxxy_fraud_engine.engine.fraudrules.ruletype.RuleType;
    import com.buxxy.buxxy_fraud_engine.engine.fraudrules.model.FraudRules;
    import lombok.Getter;
    import lombok.RequiredArgsConstructor;
    import lombok.Setter;

    import java.math.BigDecimal;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public class FraudRuleDtoForEngine {

        private long ruleId;

        private BigDecimal threshold;

        private String metadata;

        private RuleType ruleType;

        public FraudRuleDtoForEngine(FraudRules rule) {
            this.ruleId = rule.getRuleId();
            this.ruleType = rule.getRuleType();
            this.threshold = rule.getThreshold();
            this.metadata = rule.getMetadata();
        }


    }
