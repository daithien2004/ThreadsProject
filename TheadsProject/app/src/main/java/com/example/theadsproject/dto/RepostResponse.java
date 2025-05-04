package com.example.theadsproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RepostResponse {
    private Long postId;
    private String content;
    private ArrayList<String> mediaUrls;

    private String visibility;

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

    public UserResponse getRepostedBy() {
        return repostedBy;
    }

    public void setRepostedBy(UserResponse repostedBy) {
        this.repostedBy = repostedBy;
    }

    private LocalDateTime createdAt;
    private UserResponse user;         // người đăng bài gốc
    private UserResponse repostedBy;   // người repost lại
    public RepostResponse(UserResponse repostedBy, UserResponse user, LocalDateTime createdAt, String visibility, ArrayList<String> mediaUrls, String content, Long postId) {
        this.repostedBy = repostedBy;
        this.user = user;
        this.createdAt = createdAt;
        this.visibility = visibility;
        this.mediaUrls = mediaUrls;
        this.content = content;
        this.postId = postId;
    }
    // Getter và Setter
}


