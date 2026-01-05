package com.buxxy.buxxy_fraud_engine.service.authservice;

import com.buxxy.buxxy_fraud_engine.dto.auth.LoginDto;
import com.buxxy.buxxy_fraud_engine.dto.auth.LoginResponseDto;
import com.buxxy.buxxy_fraud_engine.enums.Role;
import com.buxxy.buxxy_fraud_engine.exceptions.UnauthorizedException;
import com.buxxy.buxxy_fraud_engine.jwtutil.JwtUtils;
import com.buxxy.buxxy_fraud_engine.userdetailservice.CustomUserDetailService;
import com.buxxy.buxxy_fraud_engine.userdetailservice.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {







        private final AuthenticationManager authenticationManager;
        private final CustomUserDetailService userDetailsService;
        private final JwtUtils jwtUtils;

    public LoginService(AuthenticationManager authenticationManager, CustomUserDetailService userDetailsService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponseDto login(LoginDto request) {

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUserMail(),
                                request.getUserPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
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

            return new LoginResponseDto(token);

}
}
