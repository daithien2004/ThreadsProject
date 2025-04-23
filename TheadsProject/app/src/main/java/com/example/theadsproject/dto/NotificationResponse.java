package com.example.theadsproject.dto;

public class NotificationResponse {
    private Long id;
    private String type;
    private Long postId;
    private Boolean isRead;
    private String createdAt;
    private Long senderId;
    private String senderName;
    private String senderAvatar;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, String type, Long postId, Boolean isRead, String createdAt, Long senderId, String senderName, String senderAvatar) {
        this.id = id;
        this.type = type;
        this.postId = postId;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }
}
