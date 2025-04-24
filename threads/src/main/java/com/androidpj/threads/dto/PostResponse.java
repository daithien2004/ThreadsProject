package com.androidpj.threads.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.androidpj.threads.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long postId;
    private String content;
    private List<String> mediaUrls;
    private String visibility;
    private LocalDateTime createdAt;
    private UserResponse user;

    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.content = post.getContent();
        this.visibility = post.getVisibility();
        this.createdAt = post.getCreatedAt();
        this.user = new UserResponse(post.getUser());
        this.mediaUrls = post.getMediaUrls() != null
                ? new ArrayList<>(post.getMediaUrls())
                : new ArrayList<>();
    }
}


