package com.buxxy.buxxy_fraud_engine.controller.user;

import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.dto.user.UserUpdateDto;
import com.buxxy.buxxy_fraud_engine.service.user.UserService;
import com.fasterxml.classmate.members.RawField;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDTO> viewProfile(Principal principal){
        UserResponseDTO responseDTO=userService.viewProfile(principal);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/update/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserUpdateDto> updateProfile(@RequestBody @Valid UserUpdateDto userUpdateDto, Principal principal){
        UserUpdateDto updateProfile =  userService.updateProfile(userUpdateDto,principal);
        return ResponseEntity.ok(updateProfile);
    }

    @PatchMapping("/deactivate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deActivate(Principal principal){
        userService.deActivate(principal);
        return ResponseEntity.noContent().build();
    }

}
