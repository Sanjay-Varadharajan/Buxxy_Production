package com.buxxy.buxxy_fraud_engine.repositories;

import com.buxxy.buxxy_fraud_engine.enums.Role;
import com.buxxy.buxxy_fraud_engine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserMailAndUserActiveTrue(String userMail);


    Page<User> findByUserRole(Role role, Pageable pageable);

}
