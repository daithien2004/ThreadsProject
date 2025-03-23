package com.example.theadsproject.DTO;

import java.util.List;

public class PostResponse {
    private Long id;
    private String content;
    private List<String> mediaUrls;
    private String visibility;
    private String createdAt;
    private UserResponse user;

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserResponse getUser() {
        return user;
    }
    public PostResponse(Long id, String content, List<String> mediaUrls, String visibility, String createdAt, UserResponse user) {
        this.id = id;
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.user = user;
    }
}
