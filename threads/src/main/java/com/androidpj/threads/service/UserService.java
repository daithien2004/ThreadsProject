package com.androidpj.threads.service;

import com.androidpj.threads.dto.OtpRequest;
import com.androidpj.threads.dto.UserResponse;
import com.androidpj.threads.dto.UserRequest;
import com.androidpj.threads.entity.Otp;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.OtpRepository;
import com.androidpj.threads.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public UserResponse checkLogin(UserRequest userRequest) {
        User user = userRepository.findByUsername(userRequest.getUsername());
        if (user != null && user.getPassword().equals(userRequest.getPassword())) {
            UserResponse userRespone = new UserResponse();
            userRespone.setUserId(user.getUserId());
            userRespone.setUsername(user.getUsername());
            userRespone.setUsername(user.getPassword());
            return userRespone;
        }
        return null;
    }

    public boolean register(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail()))
            return false;

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setPassword(userRequest.getPassword());
        user.setNickName(userRequest.getNickName());
        userRepository.save(user);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(5);
        String gOtp = generateOTP();
        Otp otp = new Otp(user.getEmail(), expiresAt, gOtp, now);
        otpRepository.save(otp);
        sendOtpEmail(user.getEmail(), gOtp, "Kích hoạt tài khoản của bạn");
        return true;
    }

    public String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public void sendOtpEmail(String email, String otp, String subject) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText("OTP của bạn là: ", otp);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public boolean active(OtpRequest otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        Optional<Otp> otpOptional = otpRepository.findByEmailAndOtpCode(email, otp);
        if (otpOptional.isPresent()) {
            Otp otpEntity = otpOptional.get();
            LocalDateTime now = LocalDateTime.now();

            if (now.isAfter(otpEntity.getExpiresAt())) {
                otpRepository.deleteByEmail(email);
                return false;
            }

            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setActive(true);
                userRepository.save(user);
                otpRepository.deleteById(otpEntity.getId());
                return true;
            }
        }
        return false;
    }

    public boolean resetOtp(UserRequest userRequest) {
        String gOtp = generateOTP();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(5);
        Otp otp = new Otp(userRequest.getEmail(), expiresAt, gOtp, now);
        otpRepository.save(otp);
        sendOtpEmail(userRequest.getEmail(), gOtp, "OTP đổi mật khẩu");
        return true;
    }

    public boolean resetPassword(OtpRequest otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        String password = otpRequest.getPassword();
        Optional<Otp> otpOptional = otpRepository.findByEmailAndOtpCode(email, otp);
        if (otpOptional.isPresent()) {
            Otp otpEntity = otpOptional.get();
            LocalDateTime now = LocalDateTime.now();

            if (now.isAfter(otpEntity.getExpiresAt())) {
                otpRepository.deleteByEmail(email);
                return false;
            }

            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setPassword(password);
                userRepository.save(user);
                otpRepository.deleteById(otpEntity.getId());
                return true;
            }
        }
        return false;
    }
}
