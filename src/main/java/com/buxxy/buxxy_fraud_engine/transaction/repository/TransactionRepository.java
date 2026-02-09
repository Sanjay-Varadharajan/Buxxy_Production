package com.buxxy.buxxy_fraud_engine.transaction.repository;

import com.buxxy.buxxy_fraud_engine.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    List<Transaction> findTop5ByExternalUser_eUserIdOrderByTransactionOnDesc(Long eUserId);
}
