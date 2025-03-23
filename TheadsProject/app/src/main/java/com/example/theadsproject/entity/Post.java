package com.example.theadsproject.entity;

import com.example.theadsproject.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
    private Long id;
    private String content;
    private String visibility;
    private LocalDateTime createdAt;
    private User user;
    private List<String> mediaUrls;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<String> getMediaUrls() { return mediaUrls; }
    public void setMediaUrls(List<String> mediaUrls) { this.mediaUrls = mediaUrls; }

    public Post(Long id, String content, String visibility, LocalDateTime createdAt, User user, List<String> mediaUrls) {
        this.id = id;
        this.content = content;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.user = user;
        this.mediaUrls = mediaUrls;
    }
}

