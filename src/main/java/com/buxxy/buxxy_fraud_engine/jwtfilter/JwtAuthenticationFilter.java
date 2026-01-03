package com.buxxy.buxxy_fraud_engine.jwtfilter;


import com.buxxy.buxxy_fraud_engine.jwtutil.JwtUtils;
import com.buxxy.buxxy_fraud_engine.userdetailservice.CustomUserDetailService;
import com.buxxy.buxxy_fraud_engine.userdetailservice.CustomUserDetails;
import jakarta.persistence.CascadeType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailService userDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader=request.getHeader("Authorization");
        String token=null;
        String userMail=null;
        String userRole=null;

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7).trim();
            try {
                if (jwtUtils.validateToken(token)) {
                    userMail = jwtUtils.getUserMailFromToken(token);
                    userRole = jwtUtils.getRoleFromToken(token);
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(userRole);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userMail,
                                    null,
                                    List.of(authority)
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            catch (Exception e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or Expired Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
