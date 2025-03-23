package com.example.theadsproject.DTO;

public class UserResponse {
    private Long userId;
    private String username;

    private String nickName;
    private String image;

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

    public UserResponse(Long userId, String username, String nickName, String image) {
        this.userId = userId;
        this.username = username;
        this.nickName = nickName;
        this.image = image;
    }
}
