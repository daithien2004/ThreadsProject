package com.example.theadsproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostRequest {
    private String content;
    private ArrayList<String> mediaUrls;
    private String visibility;
    private Long userId;

    public PostRequest(String content, ArrayList<String> mediaUrls, String visibility, Long userId) {
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.visibility = visibility;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMediaUrls() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
