package com.example.theadsproject.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @SerializedName("user_id")
    private Long userId;
    private String username;
    private String nickName;
    private String image;
    private String message;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
