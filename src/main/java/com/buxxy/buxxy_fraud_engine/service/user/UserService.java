package com.buxxy.buxxy_fraud_engine.service.user;

import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.dto.user.UserUpdateDto;
import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import com.buxxy.buxxy_fraud_engine.exceptions.UserNotFoundException;
import com.buxxy.buxxy_fraud_engine.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.AuditRepository;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {


    private final UserRepository userRepository;

    private final AuditRepository auditRepository;


    @Transactional(readOnly = true)
    public UserResponseDTO viewProfile(Principal principal) {
        User loggedInUser = userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName() + " not found,Login and try"));

        return new UserResponseDTO(loggedInUser);
    }

    public UserUpdateDto updateProfile(
                                                      UserUpdateDto userUpdateDto,
                                                       Principal principal) {

        User loggedInUser=userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(()->new UsernameNotFoundException(principal.getName()+" not Found,Login and try"));

        loggedInUser.setUserName(userUpdateDto.getUserName());
        User updatedUser= userRepository.save(loggedInUser);
        AuditLog auditLog=new AuditLog();
        auditLog.setUser(loggedInUser);
        auditLog.setStatus(AuditStatus.UPDATED);
        auditLog.setAction(principal.getName()+" User is Created");
        auditRepository.save(auditLog);

        return new UserUpdateDto(updatedUser);
    }

    public void deActivate(Principal principal) {
        User user=userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(()-> new UserNotFoundException(
                        principal.getName()+" Not Found,Login and try")
                );

        user.setUserActive(false);
        userRepository.save(user);
        AuditLog auditLog=new AuditLog();
        auditLog.setUser(user);
        auditLog.setStatus(AuditStatus.DEACTIVATED);
        auditLog.setAction(principal.getName()+" Got Deactivated");
        auditRepository.save(auditLog);
    }
}
