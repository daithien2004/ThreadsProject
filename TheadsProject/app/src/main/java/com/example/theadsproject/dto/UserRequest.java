package com.example.theadsproject.dto;

public class UserRequest {
    private String email;
    private String phone;
    private String username;
    private String password;

    private String nickName;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserRequest(String nickName, String username, String email, String phone, String password) {
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
    }

    public UserRequest(String email) {
        this.email = email;
    }
}
