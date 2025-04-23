package com.androidpj.threads.controller;

import com.androidpj.threads.dto.OtpRequest;
import com.androidpj.threads.dto.UserRequest;
import com.androidpj.threads.dto.UserResponse;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> checkLogin(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.checkLogin(userRequest);
        if (userResponse != null)
            return ResponseEntity.ok(userResponse);
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
}
