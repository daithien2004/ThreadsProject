package com.androidpj.threads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long receiverId;
    private Long senderId;
    private String type;
    private Long postId;

    public NotificationRequest(Long receiverId, Long senderId, String type) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.type = type;
    }
}

