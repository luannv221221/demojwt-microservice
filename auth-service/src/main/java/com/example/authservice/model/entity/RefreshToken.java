package com.example.authservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 255)
    private String token;

    private Instant expiryDate;

    // thuong thi cai nay se la khoa ngoai den bang user co
    private String username;

    private boolean isRevoked;

}
