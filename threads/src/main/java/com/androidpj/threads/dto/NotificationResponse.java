package com.androidpj.threads.dto;

import com.androidpj.threads.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.type = notification.getType();
        this.postId = notification.getPostId();
        this.isRead = notification.getIsRead();
        this.createdAt = notification.getCreatedAt().toLocalDateTime().format(FORMATTER);

        this.senderId = notification.getSender().getUserId();
        this.senderName = notification.getSender().getNickName();
        this.senderAvatar = notification.getSender().getImage();
    }
}


