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

}
