package com.androidpj.threads.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.androidpj.threads.dto.UserResponse;
import com.androidpj.threads.service.FollowService;

@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    // Theo dõi người dùng
    @PostMapping
    public ResponseEntity<String> follow(@RequestParam Long followerId,
                                         @RequestParam Long followingId) {
        followService.followUser(followerId, followingId);
        return ResponseEntity.ok("Đã theo dõi");
    }

    // Bỏ theo dõi người dùng
    @DeleteMapping
    public ResponseEntity<String> unfollow(@RequestParam Long followerId,
                                           @RequestParam Long followingId) {
        followService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok("Đã bỏ theo dõi");
    }

    // Kiểm tra có đang theo dõi không
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFollowing(@RequestParam Long followerId,
                                                  @RequestParam Long followingId) {
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(isFollowing);
    }
    @GetMapping("/users/{userId}/following")
    public ResponseEntity<List<UserResponse>> getFollowingUsers(@PathVariable Long userId) {
        List<UserResponse> following = followService.getFollowingUsers(userId);
        return ResponseEntity.ok(following);
    }

}
