package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.fruadcontrol;


import com.buxxy.buxxy_fraud_engine.dto.fraudrules.FraudRuleDtoForEngine;
import com.buxxy.buxxy_fraud_engine.dto.fraudscore.FraudScoreResponseDTO;
import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.model.AuditLogForEngine;
import com.buxxy.buxxy_fraud_engine.model.FraudScore;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.repositories.EngineAuditLogRepository;
import com.buxxy.buxxy_fraud_engine.repositories.FraudRuleRepository;
import com.buxxy.buxxy_fraud_engine.repositories.FraudScoreRepository;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FraudControlService {


    private final TransactionRepository transactionRepository;

    private final FraudScoreRepository fraudScoreRepository;

    private final FraudRuleRepository fraudRuleRepository;

    private final EngineAuditLogRepository engineAuditLogRepository;

    private final RuleApplyingService ruleApplyingService;


    private static final int BLOCK_THRESHOLD = 75;

    private static final int STEP_UP_THRESHOLD = 40;



    public Decision fraudControl(Transaction transaction,HttpServletRequest httpServletRequest) {
        Decision decisionMade=calculatedScore(transaction,httpServletRequest)
                .getDecision();

        return decisionMade;
    }


    public FraudScoreResponseDTO calculatedScore(Transaction transaction,HttpServletRequest httpServletRequest) {
        int fraudScoreInit=0;

        List<Transaction> last5Transaction=transactionRepository
                .findTop5ByUserUserIdOrderByTransactionOnDesc(transaction.getUser().getUserId());

        BigDecimal avgAmount=BigDecimal.ZERO;

        if (!last5Transaction.isEmpty()){
            avgAmount=last5Transaction.stream()
                    .map(Transaction::getTransactionAmount)
                    .reduce(BigDecimal.ZERO,BigDecimal::add)
                    .divide(BigDecimal.valueOf(last5Transaction.size()),2,RoundingMode.HALF_UP);
        }

        List<FraudRuleDtoForEngine> fraudRules=getActiveRules();

        for(FraudRuleDtoForEngine rule:fraudRules){
            if(ruleApplyingService.ruleApplies(transaction,last5Transaction,rule,avgAmount,httpServletRequest)){
                int scoreToAdd=0;

                if(rule.getMetadata()!=null && !rule.getMetadata().isEmpty()){
                    try {
                        JSONObject json = new JSONObject(rule.getMetadata());
                        scoreToAdd = json.optInt("score", 0);
                    }catch (Exception e){
                        scoreToAdd=0;
                    }
                }
                fraudScoreInit=fraudScoreInit+scoreToAdd;
            }
        }


        fraudScoreInit=Math.min(fraudScoreInit,100);

        Decision decision;

        if(fraudScoreInit>=BLOCK_THRESHOLD){
            decision=Decision.BLOCK;
        }
        else if(fraudScoreInit>=STEP_UP_THRESHOLD){
            decision=Decision.STEP_UP;
        }
        else {
            decision=Decision.ALLOW;
        }
        FraudScore fraudScore = new FraudScore();
        fraudScore.setTransaction(transaction);
        fraudScore.setRiskScore(fraudScoreInit);
        fraudScore.setDecision(decision);
        fraudScoreRepository.save(fraudScore);

        AuditLogForEngine auditLogForEngine=new AuditLogForEngine();
        auditLogForEngine.setDecision(decision);
        auditLogForEngine.setUser(transaction.getUser());
        auditLogForEngine.setTransaction(transaction);
        if(decision.isBlocked()){
            auditLogForEngine.setStatus(TransactionStatus.BLOCKED);
        }
        else if (decision.requiresStepUp()) {
            auditLogForEngine.setStatus(TransactionStatus.PENDING);
        }
        else{
            auditLogForEngine.setStatus(TransactionStatus.APPROVED);
        }

        engineAuditLogRepository.save(auditLogForEngine);

        return new FraudScoreResponseDTO(fraudScore);


    }







    public List<FraudRuleDtoForEngine> getActiveRules(){
       List<FraudRuleDtoForEngine> activeRules=fraudRuleRepository
                                                .findByIsActiveTrue()
                                                .stream()
                                                .map(FraudRuleDtoForEngine::new)
                                                .toList();

       return activeRules;
    }

    public int fraudScore(Transaction transaction,HttpServletRequest httpServletRequest){
        FraudScoreResponseDTO responseDTO=calculatedScore(transaction,httpServletRequest);
        return responseDTO.getRiskScore();
    }
}
