package com.buxxy.buxxy_fraud_engine.repositories;

import com.buxxy.buxxy_fraud_engine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUserMailAndUserActiveTrue(String userMail);
}
