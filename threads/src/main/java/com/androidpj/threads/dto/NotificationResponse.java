package com.androidpj.threads.dto;

import com.androidpj.threads.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String type;
    private Long postId;
    private Boolean isRead;
    private String createdAt;

    private Long senderId;
    private String senderName;
    private String senderAvatar;

    public NotificationResponse (Notification noti) {
        this.setId(noti.getId());
        this.setType(noti.getType());
        this.setPostId(noti.getPostId());
        this.setIsRead(noti.getIsRead());
        this.setCreatedAt(noti.getCreatedAt().toString());
        this.setSenderId(noti.getSender().getUserId());
        this.setSenderName(noti.getSender().getNickName());
        this.setSenderAvatar(noti.getSender().getImage());
    }
}

