package com.example.theadsproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}
