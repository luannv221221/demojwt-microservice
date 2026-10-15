package com.exam.apigateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtils {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    // chuyen secret thay key Object chay tot voi thuat toan HS256
    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // lay cac thong tin tu token
    public Claims getAllClaim(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // check token co hop le hay khong
    public boolean isInvalid(String token){
        System.out.println(secretKey);
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return false;
        } catch (Exception e){
            System.out.println("JWT ERR"+e.getMessage());
            return true;
        }
    }

}
