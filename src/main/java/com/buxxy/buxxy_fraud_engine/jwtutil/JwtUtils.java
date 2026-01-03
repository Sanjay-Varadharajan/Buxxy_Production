package com.buxxy.buxxy_fraud_engine.jwtutil;

import com.buxxy.buxxy_fraud_engine.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtils {

    private final Key key;
    private final int jwtExpirationMs;
    private final long ACCESS_TOKEN_EXPIRY = 15 * 60 * 1000;

    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMs}") int jwtExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String userMail, Role userRole){
        return Jwts.builder()
                .setSubject(userMail)
                .claim("role",userRole.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserMailFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role",String.class);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException e) {
            throw e;
        }
        catch (JwtException jwtException){
            return false;
        }
    }
}
