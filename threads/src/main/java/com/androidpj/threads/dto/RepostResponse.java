package com.androidpj.threads.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepostResponse {
    private Long postId;
    private String content;
    private Set<String> mediaUrls;
    private String visibility;
    private LocalDateTime createdAt;
    private UserResponse user; // Người đăng bài gốc
    private UserResponse repostedBy; // Người repost lại bài

    // Constructor to map Post and User (người repost)
    public RepostResponse(Post post, User repostedByUser) {
        this.postId = post.getPostId();
        this.content = post.getContent();
        this.visibility = post.getVisibility();
        this.createdAt = post.getCreatedAt();
        this.user = new UserResponse(post.getUser()); // Người đăng bài gốc
        this.repostedBy = repostedByUser != null ? new UserResponse(repostedByUser) : null; // Người repost lại
        this.mediaUrls = post.getMediaUrls() != null	
                ? post.getMediaUrls()
                : Set.of(); // Default empty list if no mediaUrls
    }
}
