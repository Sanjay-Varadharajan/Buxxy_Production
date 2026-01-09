package com.buxxy.buxxy_fraud_engine.service.engine.fruadcontrol;



import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FraudControlService {

    private final TransactionRepository transactionRepository;

    public Decision fraudControl(Transaction transaction){
        List<Transaction> last5Transactions=
                transactionRepository.findTop5ByUserUserIdOrderByTransactionOnDesc(transaction.getUser().getUserId());

        if (last5Transactions.isEmpty()){
            return Decision.ALLOW;
        }

        BigDecimal avgAmount=
                last5Transactions
                        .stream()
                        .map(Transaction::getTransactionAmount)
                        .reduce(BigDecimal.ZERO,BigDecimal::add)
                        .divide(new BigDecimal(last5Transactions.size()),2, RoundingMode.HALF_UP);

        BigDecimal currentTransaction
                =transaction.getTransactionAmount();

        if(currentTransaction.compareTo(avgAmount.multiply(new BigDecimal("5")))>0){
            return Decision.BLOCK;
        }
        else if (currentTransaction.compareTo(avgAmount.multiply(new BigDecimal("2"))) > 0) {
            return Decision.STEP_UP;
        }
        else {
            return Decision.ALLOW;
        }
    }



}
