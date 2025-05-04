package com.example.theadsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String email;
    private String phone;
    private String username;
    private String password;
    private String nickName;
    private String bio;
    private String image;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UserRequest(String email) {
        this.email = email;
    }

    public UserRequest(String nickName, String username, String email, String phone,  String password) {
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
    }

    public UserRequest(String username, String nickName, String bio, String image) {
        this.nickName = nickName;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }
}
