package com.buxxy.buxxy_fraud_engine.externaluser.repository;

import com.buxxy.buxxy_fraud_engine.externaluser.model.ExternalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser,Integer> {



}
