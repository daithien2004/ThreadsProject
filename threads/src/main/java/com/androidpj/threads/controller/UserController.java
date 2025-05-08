package com.androidpj.threads.controller;

import com.androidpj.threads.dto.OtpRequest;
import com.androidpj.threads.dto.UpdateBioRequest;
import com.androidpj.threads.dto.UpdateProfileRequest;
import com.androidpj.threads.dto.UserRequest;
import com.androidpj.threads.dto.UserResponse;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.service.UserService;
import com.androidpj.threads.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> checkLogin(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.checkLogin(userRequest);
        if (userResponse != null) {
            String token = jwtUtil.generateToken(userRequest.getUsername());
            userResponse.setToken(token);
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.badRequest().body(new UserResponse("Sai tài khoản hoặc mật khẩu"));
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody UserRequest userRequest) {
        boolean register = userService.register(userRequest);
        if (register) {
            return ResponseEntity.ok(true);
        }
        else
            return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/activate")
    public ResponseEntity<Boolean> activate(@RequestBody OtpRequest otpRequest) {
        boolean success = userService.active(otpRequest);
        if (success) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/resetOtp")
    public ResponseEntity<Boolean> resetOtp(@RequestBody UserRequest userRequest) {
        boolean success = userService.resetOtp(userRequest);
        if (success) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Boolean> resetPassword(@RequestBody OtpRequest otpRequest) {
        boolean success = userService.resetPassword(otpRequest);
        if (success) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsersSortedByFollowers() {
        List<UserResponse> users = userService.getAllUsersSortedByFollowers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/search")
    public List<UserResponse> searchUsers(@RequestParam("keyword") String keyword) {
        return userService.searchUsersByKeyword(keyword);
    }
    // cap nhat bio
    @PutMapping("/{id}/bio")
    public ResponseEntity<?> updateBio(@PathVariable Long id, @RequestBody UpdateBioRequest request) {
        try {
            userService.updateUserBio(id, request.getBio());
            return ResponseEntity.ok("Cập nhật bio thành công");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại");
        }
    }
    
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateProfileRequest userUpdateRequest) {
        UserResponse updatedUser = userService.updateUser(userId, userUpdateRequest);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        if (userResponse != null) {
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
