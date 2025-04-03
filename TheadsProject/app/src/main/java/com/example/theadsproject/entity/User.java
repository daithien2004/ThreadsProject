package com.example.theadsproject.entity;


import com.google.gson.annotations.SerializedName;

import java.util.Set;


public class User {
    @SerializedName("user_id")
    private Long userId;
    private String email;
    @SerializedName("nick_name")
    private String nickName;
    private String image;
    private String username;
    private String password;
    private String bio;
    private String phone;

    public User(Long userId, String email, String nickName, String image, String username, String bio, String phone) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.image = image;
        this.username = username;
        this.bio = bio;
        this.phone = phone;
    }

    public User(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
