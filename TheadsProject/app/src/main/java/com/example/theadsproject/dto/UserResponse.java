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
}
