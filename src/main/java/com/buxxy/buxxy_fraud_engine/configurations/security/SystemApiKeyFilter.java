package com.buxxy.buxxy_fraud_engine.configurations.security;

import com.buxxy.buxxy_fraud_engine.model.SystemApiKeyAttempt;
import com.buxxy.buxxy_fraud_engine.repositories.ApiKeyAttemptRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SystemApiKeyFilter extends OncePerRequestFilter {


    private final ApiKeyAttemptRepository apiKeyAttemptRepository;

    private static final Logger logger = LoggerFactory.getLogger(SystemApiKeyFilter.class);

    @Value("${system.api-keys}")
    private String apiKeys;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String systemKey=request.getHeader("X-SYSTEM-KEY");

        List<String> validKeys= Arrays.asList(apiKeys.split(","));

        boolean valid=systemKey!=null && validKeys.stream().anyMatch(k->k.trim().equals(systemKey.trim()));
        if(!valid){
           logger.warn("Unauthorized system key attempt from IP: {} to endpoint: {}",
                   request.getRemoteAddr(), request.getRequestURI());
            SystemApiKeyAttempt systemApiKeyAttempt=new SystemApiKeyAttempt();
            systemApiKeyAttempt.setAttemptedKey(systemKey);
            systemApiKeyAttempt.setIpAddress(request.getRemoteAddr());
            systemApiKeyAttempt.setAttemptedAt(LocalDateTime.now());
            systemApiKeyAttempt.setEndpoint(request.getRequestURI());
            systemApiKeyAttempt.setSuccess(false);
            apiKeyAttemptRepository.save(systemApiKeyAttempt);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"INVALID_SYSTEM_KEY\",\"message\":\"Access denied\"}");
            return;
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path=request.getRequestURI();
        return !path.startsWith("/api/system/");
    }
}
