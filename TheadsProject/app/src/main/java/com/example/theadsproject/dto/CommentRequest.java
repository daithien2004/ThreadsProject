package com.example.theadsproject.dto;

import java.util.ArrayList;

public class CommentRequest {
    private String content;
    private ArrayList<String> mediaUrls;
    private String visibility;
    private Long userId;
    private Long postId;

    public CommentRequest(String content, ArrayList<String> mediaUrls, String visibility, Long userId, Long postId) {
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.visibility = visibility;
        this.userId = userId;
        this.postId = postId;
    }
}