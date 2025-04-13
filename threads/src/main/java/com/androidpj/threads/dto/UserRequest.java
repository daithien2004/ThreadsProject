package com.androidpj.threads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {
    private String nickName;
    private String username;
    private String email;
    private String phone;
    private String password;
}
