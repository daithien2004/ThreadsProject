package com.androidpj.threads.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.androidpj.threads.dto.UserResponse;
import com.androidpj.threads.entity.Follow;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.FollowRepository;
import com.androidpj.threads.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("Không thể tự theo dõi chính mình.");
        }

        if (!followRepository.existsByFollower_UserIdAndFollowing_UserId(followerId, followingId)) {
            User follower = userRepository.findById(followerId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng theo dõi"));
            User following = userRepository.findById(followingId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng được theo dõi"));

            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followRepository.save(follow);
        }
    }
    
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        followRepository.deleteByFollower_UserIdAndFollowing_UserId(followerId, followingId);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollower_UserIdAndFollowing_UserId(followerId, followingId);
    }
    public List<UserResponse> getFollowingUsers(Long userId) {
        List<User> following = followRepository.findFollowingByUserId(userId);
        return following.stream()
                        .map(UserResponse::new)
                        .collect(Collectors.toList());
    }

    public long getFollowerCount(Long userId) {
        return followRepository.countFollowersByUserId(userId);
    }
}
