package com.buxxy.buxxy_fraud_engine.controller.auth;

import com.buxxy.buxxy_fraud_engine.dto.auth.SignUpDto;
import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.idempotency.service.IdempotentService;
import com.buxxy.buxxy_fraud_engine.service.authservice.SignupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class SignupController {

    private final SignupService signupService;
    private final IdempotentService idempotentService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody SignUpDto signupUser){
        UserResponseDTO response=idempotentService.executeIdempotent(
                idempotencyKey,
                signupUser,
                UserResponseDTO.class,
                ()->signupService.signup(signupUser)
        );

        return ResponseEntity.ok(response);
    }

}
