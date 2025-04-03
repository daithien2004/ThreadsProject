package com.androidpj.threads.repository;

import com.androidpj.threads.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndOtpCode(String email, String otp);
    void deleteByEmail(String email);
    void deleteByEmailAndOtpCode(String email, String otp);
    void deleteById(Long id);
}
