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

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UserRequest(String email) {
        this.email = email;
    }
}
