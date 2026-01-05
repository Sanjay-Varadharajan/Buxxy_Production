package com.buxxy.buxxy_fraud_engine.controller.user;

import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> viewProfile(Principal principal){
        return userService.viewProfile(principal);
    }
}
