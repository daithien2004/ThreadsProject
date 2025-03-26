package com.example.theadsproject.entity;


import com.google.gson.annotations.SerializedName;

import java.util.Set;


public class User {
    @SerializedName("user_id")
    private Long userId;

    @SerializedName("email")
    private String email;

    @SerializedName("nick_name")
    private String nickName;

    @SerializedName("image")
    private String image;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public User(Long user_id, String email, String nickName, String image, String username, String password) {
        this.userId = user_id;
        this.email = email;
        this.nickName = nickName;
        this.image = image;
        this.username = username;
        this.password = password;
    }

    public Long getUser_id() {
        return userId;
    }

    public void setUser_id(Long user_id) {
        this.userId = user_id;
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
}
