package com.buxxy.buxxy_fraud_engine.security.controller;

import com.buxxy.buxxy_fraud_engine.security.authDto.LoginDto;
import com.buxxy.buxxy_fraud_engine.security.authDto.LoginResponseDto;
import com.buxxy.buxxy_fraud_engine.security.authservice.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody @Valid LoginDto request) {

        return ResponseEntity.ok(loginService.login(request));
    }
}
