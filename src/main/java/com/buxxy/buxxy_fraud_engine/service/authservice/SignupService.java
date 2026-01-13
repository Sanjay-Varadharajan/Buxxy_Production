package com.buxxy.buxxy_fraud_engine.service.authservice;


import com.buxxy.buxxy_fraud_engine.dto.auth.SignUpDto;
import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import com.buxxy.buxxy_fraud_engine.enums.Role;
import com.buxxy.buxxy_fraud_engine.exceptions.UserAlreadyExistsException;
import com.buxxy.buxxy_fraud_engine.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.AuditRepository;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SignupService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuditRepository auditRepository;

    public UserResponseDTO signup(@Valid SignUpDto signupUser) {
        userRepository.findByUserMailAndUserActiveTrue(signupUser.getUserMail())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException(signupUser.getUserMail()+ " Already Exists");
                });
        User user=new User();

        user.setUserName(signupUser.getUserName());
        user.setUserMail(signupUser.getUserMail());
        user.setUserPassword(passwordEncoder.encode(signupUser.getUserPassword()));
        user.setUserRole(Role.ROLE_USER);
        userRepository.save(user);
        AuditLog auditLog=new AuditLog();
        auditLog.setUser(user);
        auditLog.setStatus(AuditStatus.SIGNUP);
        auditLog.setAction("new user "+signupUser.getUserMail()+" signed Up");
        auditRepository.save(auditLog);
        return new UserResponseDTO(user);
    }
}
