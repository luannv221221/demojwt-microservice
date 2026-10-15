package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequest;
import com.example.authservice.dto.LoginResponse;
import com.example.authservice.dto.RefreshRequest;
import com.example.authservice.model.entity.RefreshToken;
import com.example.authservice.sercurity.JwtProvider;
import com.example.authservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
// mo phong de demo con chung ta van can chuan cac thanh phan Model, Entity, DTO, Service ..
public class AuthController {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Mock Dataa
    private final String userName = "demo";
    private final String passwordHash = passwordEncoder.encode("123456");

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        // check gia dinh
        if(userName.equals(loginRequest.getUsername()) && passwordEncoder.matches(loginRequest.getPassword(),passwordHash)){
            // Gia su dang nhap thanh cong

            String accessToken = jwtProvider.generateAccessToken(loginRequest.getUsername(),"ADMIN","US11100901920");
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userName);
            LoginResponse response = LoginResponse.builder()
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .refreshToken(refreshToken.getToken())
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Sai thong tin dang nhap");
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest refreshRequest){

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshRequest.getRefreshToken());

        // tao accesstoken moi
        String  accessToken = jwtProvider.generateAccessToken(refreshToken.getUsername(),"ADMIN","US11100901920");
        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .refreshToken(refreshToken.getToken())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String test(){
        return "a";
    }
}
