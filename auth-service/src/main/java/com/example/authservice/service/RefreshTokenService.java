package com.example.authservice.service;

import com.example.authservice.model.entity.RefreshToken;
import org.springframework.stereotype.Service;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);
    RefreshToken findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
}
