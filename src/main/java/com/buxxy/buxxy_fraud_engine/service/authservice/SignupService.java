package com.buxxy.buxxy_fraud_engine.service.authservice;


import com.buxxy.buxxy_fraud_engine.dto.auth.SignUpDto;
import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.enums.Role;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public SignupService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO signup(@Valid SignUpDto signupUser) {
        userRepository.findByUserMailAndUserActiveTrue(signupUser.getUserMail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email already registered");
                });
        User user=new User();

        user.setUserName(signupUser.getUserName());
        user.setUserMail(signupUser.getUserMail());
        user.setUserPassword(passwordEncoder.encode(signupUser.getUserPassword()));
        user.setUserRole(Role.ROLE_USER);

        userRepository.save(user);

        return new UserResponseDTO(user);
    }
}
