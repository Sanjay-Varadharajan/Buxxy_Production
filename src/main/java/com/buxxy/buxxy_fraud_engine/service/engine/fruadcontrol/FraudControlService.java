package com.buxxy.buxxy_fraud_engine.service.engine.fruadcontrol;


import com.buxxy.buxxy_fraud_engine.dto.fraudscore.FraudScoreResponseDTO;
import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.model.FraudScore;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.repositories.FraudScoreRepository;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FraudControlService {


    private final TransactionRepository transactionRepository;

    private final FraudScoreRepository fraudScoreRepository;

    private static final int BLOCK_THRESHOLD = 75;

    private static final int STEP_UP_THRESHOLD = 40;

    private static final int HIGH_AMOUNT_SCORE = 50;

    private static final int MEDIUM_AMOUNT_SCORE = 25;

    private static final int NEW_CITY_SCORE = 50;


    public Decision fraudControl(Transaction transaction) {
        Decision decisionMade=calculatedScore(transaction)
                .getDecision();

        return decisionMade;
    }


    public FraudScoreResponseDTO calculatedScore(Transaction transaction) {
        int score = 0;
        List<Transaction> last5Transactions =
                transactionRepository.findTop5ByUserUserIdOrderByTransactionOnDesc(transaction.getUser().getUserId());


        BigDecimal avgAmount =  BigDecimal.ZERO;
               if(!last5Transactions.isEmpty()){
                   avgAmount=last5Transactions
                           .stream()
                           .map(Transaction::getTransactionAmount)
                           .reduce(BigDecimal.ZERO, BigDecimal::add)
                           .divide(BigDecimal.valueOf(last5Transactions.size()), 2, RoundingMode.HALF_UP);
               }

        BigDecimal currentTransaction
                = transaction.getTransactionAmount();


        if (currentTransaction.compareTo(avgAmount.multiply(new BigDecimal("5"))) > 0) {
            score = score + HIGH_AMOUNT_SCORE;
        } else if (currentTransaction.compareTo(avgAmount.multiply(new BigDecimal("2"))) > 0) {
            score = score + MEDIUM_AMOUNT_SCORE;
        }

        boolean isNewCity = last5Transactions.stream()
                .map(Transaction::getTransactionLocation)
                .filter(loc->loc!=null)
                .noneMatch(loc->loc.equalsIgnoreCase(transaction.getTransactionLocation()));

        if(isNewCity){
            score =score+NEW_CITY_SCORE;
        }
        score = Math.min(score, 100);


        FraudScore fraudScore =
                new FraudScore();
        fraudScore.setRiskScore(score);
        fraudScore.setTransaction(transaction);
        if (score >= BLOCK_THRESHOLD) {
            fraudScore.setDecision(Decision.BLOCK);
        } else if (score >= STEP_UP_THRESHOLD) {
            fraudScore.setDecision(Decision.STEP_UP);
        } else {
            fraudScore.setDecision(Decision.ALLOW);
        }
        fraudScoreRepository.save(fraudScore);

        return new FraudScoreResponseDTO(fraudScore);


    }
}
