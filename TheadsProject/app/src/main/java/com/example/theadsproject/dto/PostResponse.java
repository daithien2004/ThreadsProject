package com.example.theadsproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostResponse implements BindableContent {
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
    public PostResponse() {}

    // Implement BindableContent
    @Override
    public Long getId() {
        return postId;
    }

    @Override
    public ArrayList<String> getMediaUrls() {
        return mediaUrls;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getVisibility() {
        return visibility;
    }

    @Override
    public UserResponse getUser() {
        return user;
    }

    @Override
    public Boolean getIsLoved() {
        return isLoved;
    }

    @Override
    public void setIsLoved(boolean loved) {
        isLoved = loved;
    }

    public Long getPostId() { return postId; }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMediaUrls(ArrayList<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

}
