package com.androidpj.threads.service;

import com.androidpj.threads.dto.OtpRequest;
import com.androidpj.threads.dto.UpdateProfileRequest;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


//    public User getUserById(Long userId) {
//        return userRepository.findByUserId(userId);
//    }
    public UserResponse getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new UserResponse(user); // hoặc chuyển đổi sang DTO theo cách bạn làm
        } else {
            return null;
        }
    }

    public UserResponse checkLogin(UserRequest userRequest) {
        User user = userRepository.findByUsername(userRequest.getUsername());

        if (user != null && user.getPassword().equals(userRequest.getPassword())) {
            return new UserResponse(user);
        }
        return null;
    }

    public boolean register(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

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


//        sendOtpEmail(user.getEmail(), gOtp, "Kích hoạt tài khoản của bạn");

        // 3. Gửi mail OTP ở background (không chặn luồng)
        CompletableFuture.runAsync(() -> {
            try {
                sendOtpEmail(user.getEmail(), gOtp, "Kích hoạt tài khoản của bạn");
            } catch (Exception e) {
                // Log lỗi nếu gửi mail thất bại
                System.err.println("Lỗi khi gửi mail: " + e.getMessage());
                e.printStackTrace();
            }
        });

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

            helper.setText("OTP của bạn là: " + otp);

            javaMailSender.send(message);
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
                otpRepository.deleteById(otpEntity.getId());
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


        // 3. Gửi mail OTP ở background (không chặn luồng)
        CompletableFuture.runAsync(() -> {
            try {
                sendOtpEmail(userRequest.getEmail(), gOtp, "OTP đổi mật khẩu");
            } catch (Exception e) {
                // Log lỗi nếu gửi mail thất bại
                System.err.println("Lỗi khi gửi mail: " + e.getMessage());
                e.printStackTrace();
            }
        });

//        sendOtpEmail(userRequest.getEmail(), gOtp, "OTP đổi mật khẩu");
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
                otpRepository.deleteById(otpEntity.getId());
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
    public List<UserResponse> getAllUsersSortedByFollowers() {
        List<User> users = userRepository.findAll();

        // Sắp xếp theo số người theo dõi giảm dần
        users.sort((u1, u2) -> Integer.compare(u2.getFollowers().size(), u1.getFollowers().size()));

        List<UserResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserResponse(user));
        }
        return result;
    }

    public List<UserResponse> searchUsersByKeyword(String keyword) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCaseOrNickNameContainingIgnoreCase(keyword, keyword);
        List<UserResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserResponse(user));
        }
        return result;
    }
    
    public void updateUserBio(Long userId, String newBio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng"));

        user.setBio(newBio);
        userRepository.save(user);
    }
    
 // Cập nhật thông tin người dùng
    public UserResponse updateUser(Long userId, UpdateProfileRequest userUpdateRequest) {
        Optional<User> existingUserOpt = userRepository.findById(userId);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Cập nhật các thông tin
            existingUser.setUsername(userUpdateRequest.getUsername());
            existingUser.setNickName(userUpdateRequest.getNickName());
            existingUser.setBio(userUpdateRequest.getBio());
            
            // Nếu có ảnh đại diện mới
            if (userUpdateRequest.getImage() != null) {
                existingUser.setImage(userUpdateRequest.getImage());
            }

            // Lưu lại thông tin đã cập nhật
            userRepository.save(existingUser);

            // Trả về UserResponse bằng cách trực tiếp tạo đối tượng từ User
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(existingUser.getUserId());
            userResponse.setUsername(existingUser.getUsername());
            userResponse.setNickName(existingUser.getNickName());
            userResponse.setBio(existingUser.getBio());
            userResponse.setImage(existingUser.getImage());

            return userResponse;
        } else {
            return null; // Người dùng không tồn tại
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
