package com.buxxy.buxxy_fraud_engine.service.authservice;

import com.buxxy.buxxy_fraud_engine.dto.auth.LoginDto;
import com.buxxy.buxxy_fraud_engine.dto.auth.LoginResponseDto;
import com.buxxy.buxxy_fraud_engine.enums.AuditStatus;
import com.buxxy.buxxy_fraud_engine.enums.Role;
import com.buxxy.buxxy_fraud_engine.exceptions.UnauthorizedException;
import com.buxxy.buxxy_fraud_engine.jwtutil.JwtUtils;
import com.buxxy.buxxy_fraud_engine.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.AuditRepository;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import com.buxxy.buxxy_fraud_engine.userdetailservice.CustomUserDetailService;
import com.buxxy.buxxy_fraud_engine.userdetailservice.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

        private final AuthenticationManager authenticationManager;

        private final CustomUserDetailService userDetailsService;

        private final JwtUtils jwtUtils;

        private final UserRepository userRepository;

        private final AuditRepository auditRepository;


    public LoginResponseDto login(LoginDto request) {

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUserMail(),
                                request.getUserPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                AuditLog failedAuditLog=new AuditLog();
                failedAuditLog.setAction("Failed login attempt for " + request.getUserMail());
                failedAuditLog.setStatus(AuditStatus.FAILURE);
                auditRepository.save(failedAuditLog);
                throw new UnauthorizedException("Invalid credentials");
            }

            CustomUserDetails userDetails =
                    (CustomUserDetails) userDetailsService
                            .loadUserByUsername(request.getUserMail());

            String role = userDetails.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority();

            String token = jwtUtils.generateToken(
                    userDetails.getUsername(),
                    Role.valueOf(role)
            );

            User user=userRepository
                    .findByUserMailAndUserActiveTrue(request.getUserMail())
                    .orElseThrow(()->
                            new UsernameNotFoundException(request.getUserMail()+" User not found after authentication"));
            AuditLog successAuditLog=new AuditLog();
        successAuditLog.setUser(user);
        successAuditLog.setStatus(AuditStatus.LOGIN);
        successAuditLog.setAction(user.getUserMail()+" User Logged In");
        auditRepository.save(successAuditLog);

        return new LoginResponseDto(token);
}
}
