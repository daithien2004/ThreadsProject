package com.androidpj.threads.dto;

import com.androidpj.threads.entity.User;
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

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickName = user.getNickName();
        this.image = user.getImage();
        this.bio = user.getBio();
        this.phone = user.getPhone();
        this.email = user.getEmail();
    }

    public UserResponse(String message) {
        this.message = message;
    }
}
