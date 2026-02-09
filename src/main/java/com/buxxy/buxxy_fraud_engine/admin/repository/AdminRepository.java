package com.buxxy.buxxy_fraud_engine.admin.repository;

import com.buxxy.buxxy_fraud_engine.admin.model.Admin;
import com.buxxy.buxxy_fraud_engine.security.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUserMailAndUserActiveTrue(String userMail);


    Page<Admin> findByUserRole(Role role, Pageable pageable);



}
