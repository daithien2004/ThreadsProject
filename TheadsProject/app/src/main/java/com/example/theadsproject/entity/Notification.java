package com.example.theadsproject.entity;


import java.sql.Timestamp;

public class Notification {
    private Long id;
    private User sender;
    private User receiver;
    private String type;
    private Long postId;
    private Boolean isRead = false;
    private Timestamp createdAt;

    public Notification(Long id, User sender, User receiver, String type, Long postId, Boolean isRead, Timestamp createdAt) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.postId = postId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
