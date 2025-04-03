package com.androidpj.threads.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Otp(String email, LocalDateTime expiresAt, String otpCode, LocalDateTime createdAt) {
        this.email = email;
        this.expiresAt = expiresAt;
        this.otpCode = otpCode;
        this.createdAt = createdAt;
    }
}
