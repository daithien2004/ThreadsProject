package com.example.theadsproject.entity;

import com.example.theadsproject.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private Long postId;
    private String content;
    private String visibility;
    private LocalDateTime createdAt;
    private User user;
    private ArrayList<String> mediaUrls;

    // Getters và Setters
    public Long getId() { return postId; }
    public void setId(Long id) { this.postId = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<String> getMediaUrls() { return mediaUrls; }
    public void setMediaUrls(ArrayList<String> mediaUrls) { this.mediaUrls = mediaUrls; }

    public Post(Long postId, String content, String visibility, LocalDateTime createdAt, User user, ArrayList<String> mediaUrls) {
        this.postId = postId;
        this.content = content;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.user = user;
        this.mediaUrls = mediaUrls;
    }
}

