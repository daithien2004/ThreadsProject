package com.androidpj.threads.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final byte[] secretKey; // Sử dụng byte array ngay từ đầu

    public JwtUtil() {
        this.secretKey = "daithien123456sieunhangao12334victory123doraemon".getBytes(StandardCharsets.UTF_8);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 giờ
                .signWith(SignatureAlgorithm.HS256, secretKey) // Sử dụng byte array
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey) // Sử dụng cùng secretKey byte array
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Log lỗi để debug
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
