package com.buxxy.buxxy_fraud_engine.security.authservice;

import com.buxxy.buxxy_fraud_engine.security.authDto.LoginDto;
import com.buxxy.buxxy_fraud_engine.security.authDto.LoginResponseDto;
import com.buxxy.buxxy_fraud_engine.auditlog.auditstatus.AuditStatus;
import com.buxxy.buxxy_fraud_engine.security.role.Role;
import com.buxxy.buxxy_fraud_engine.exceptionhandling.exceptions.UnauthorizedException;
import com.buxxy.buxxy_fraud_engine.security.jwt.jwtutil.JwtUtils;
import com.buxxy.buxxy_fraud_engine.admin.model.Admin;
import com.buxxy.buxxy_fraud_engine.auditlog.model.AuditLog;
import com.buxxy.buxxy_fraud_engine.admin.repository.AdminRepository;
import com.buxxy.buxxy_fraud_engine.auditlog.repository.AuditRepository;
import com.buxxy.buxxy_fraud_engine.security.userdetailservice.CustomUserDetailService;
import com.buxxy.buxxy_fraud_engine.security.userdetailservice.CustomUserDetails;
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

        private final AdminRepository adminRepository;

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

            Admin user=adminRepository
                    .findByUserMailAndUserActiveTrue(request.getUserMail())
                    .orElseThrow(()->
                            new UsernameNotFoundException(request.getUserMail()+" User not found after authentication"));
            AuditLog successAuditLog=new AuditLog();
        successAuditLog.setAdmin(user);
        successAuditLog.setStatus(AuditStatus.LOGIN);
        successAuditLog.setAction(user.getUserMail()+" User Logged In");
        auditRepository.save(successAuditLog);

        return new LoginResponseDto(token);
}
}
