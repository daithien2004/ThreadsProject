package com.example.theadsproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostResponse {
    private Long postId;
    private String content;
    private ArrayList<String> mediaUrls;
    private String visibility;
    private LocalDateTime createdAt;
    private UserResponse user;
    private boolean isLoved = false;

    public PostResponse(String content, ArrayList<String> mediaUrls, String visibility, UserResponse user) {
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.visibility = visibility;
        this.user = user;
    }
    public PostResponse(Long postId, String content, ArrayList<String> mediaUrls, String visibility, LocalDateTime createdAt, UserResponse user) {
        this.postId = postId;
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(ArrayList<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public boolean isLoved() {
        return isLoved;
    }

    public void setLoved(boolean loved) {
        isLoved = loved;
    }
}
