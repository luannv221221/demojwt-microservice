package com.example.authservice.sercurity;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;
    @Value("${spring.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // chuyen secret thay key Object chay tot voi thuat toan HS256
    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Tạo ra Access Token
    public String generateAccessToken(String username,String role,String userId){
        return Jwts.builder()
                .setSubject(username)
                .claim("userId",userId)
                .claim("name",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();

    }

    // Validate Token
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception){
            return false;
        }

    }
}
