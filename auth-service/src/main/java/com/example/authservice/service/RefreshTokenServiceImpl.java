package com.example.authservice.service;

import com.example.authservice.model.entity.RefreshToken;
import com.example.authservice.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenDurationMs = 7L * 24 * 60 * 60 * 1000;
    @Override
    @Transactional
    public RefreshToken createRefreshToken(String username) {
        // neu co roi thi xoa
        refreshTokenRepository.deleteAllByUsername(username);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(()->new RuntimeException("Refresh token ko co"));
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Het han reffesh");
        }
        return token;
    }
}
