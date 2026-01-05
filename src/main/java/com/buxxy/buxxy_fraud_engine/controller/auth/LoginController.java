package com.buxxy.buxxy_fraud_engine.controller.auth;

import com.buxxy.buxxy_fraud_engine.dto.auth.LoginDto;
import com.buxxy.buxxy_fraud_engine.dto.auth.LoginResponseDto;
import com.buxxy.buxxy_fraud_engine.service.authservice.LoginService;
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
