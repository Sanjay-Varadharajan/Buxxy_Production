package com.buxxy.buxxy_fraud_engine.service.user;

import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.exceptions.UserNotFoundException;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public ResponseEntity<UserResponseDTO> viewProfile(Principal principal) {
        User loggedInUser = userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName() + " not found"));

        return ResponseEntity.ok(new UserResponseDTO(loggedInUser));
    }
}
