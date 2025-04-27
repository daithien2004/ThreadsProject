package com.example.theadsproject.dto;

import com.example.theadsproject.entity.User;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String bio;
    private String email;
    private String image;
    private String nickName;
    private String username;
    private String phone;
    private String message;
    private int followerCount;

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    private boolean isFollowing;

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickName = user.getNickName();
        this.image = user.getImage();
        this.bio = user.getBio();
        this.phone = user.getPhone();
        this.email = user.getEmail();
    }
    public UserResponse(Long userId, String username, String nickName, String bio, String image) {
        this.userId = userId;
        this.username = username;
        this.nickName = nickName;
        this.bio = bio;
        this.image = image;
    }

    public UserResponse(Long userId) {
        this.userId = userId;
    }

    public UserResponse(Long userId, String username, String nickName, String image) {
        this.userId = userId;
        this.username = username;
        this.nickName = nickName;
        this.image = image;
    }

    public UserResponse(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
