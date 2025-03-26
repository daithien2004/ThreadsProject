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
    private String username;
    private String nickName;
    private String image;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickName = user.getNickName();
        this.image = user.getImage();
    }
}
