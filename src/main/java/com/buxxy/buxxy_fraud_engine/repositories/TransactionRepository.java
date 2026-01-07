package com.buxxy.buxxy_fraud_engine.repositories;

import com.buxxy.buxxy_fraud_engine.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    Page<Transaction> findByUser_UserMail(String userMail, Pageable pageable);
}
