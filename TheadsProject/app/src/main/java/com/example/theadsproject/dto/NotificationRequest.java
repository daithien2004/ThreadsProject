package com.example.theadsproject.dto;

public class NotificationRequest {
    private Long receiverId;
    private Long senderId;
    private String type;
    private Long postId;

    public NotificationRequest(Long receiverId, Long senderId, String type, Long postId) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.type = type;
        this.postId = postId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
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
}
