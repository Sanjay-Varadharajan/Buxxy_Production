package com.buxxy.buxxy_fraud_engine.controller.transaction;

import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionCreateDTO;
import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionResponseDTO;
import com.buxxy.buxxy_fraud_engine.model.Transaction;

public interface TransactionService {

    TransactionResponseDTO executeTransaction(TransactionCreateDTO transactionCreateDTO) throws Exception;

    Transaction findTransactionById(long transactionId) throws Exception;

}
