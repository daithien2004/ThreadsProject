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
}

