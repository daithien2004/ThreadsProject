package com.example.theadsproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponse implements BindableContent {
    private Long commentId;
    String content;
    private ArrayList<String> mediaUrls;
    private LocalDateTime createdAt;
    private String visibility;
    private UserResponse user;
    private PostResponse post;
    private boolean isLoved = false;

    // Thêm trường để chứa danh sách replies
    private List<CommentResponse> replies;
    // Thêm trường để xác định level của comment
    private int level;

    public CommentResponse(Long commentId, String content, ArrayList<String> mediaUrls, LocalDateTime createAt, String visibility, UserResponse user, PostResponse post) {
        this.commentId = commentId;
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.createdAt = createAt;
        this.visibility = visibility;
        this.user = user;
        this.post = post;
    }

    public CommentResponse(Long commentId, String content, ArrayList<String> mediaUrls, LocalDateTime createAt, String visibility, UserResponse user, PostResponse post, List<CommentResponse> replies, int level) {
        this.commentId = commentId;
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.createdAt = createAt;
        this.visibility = visibility;
        this.user = user;
        this.post = post;
        this.replies = replies;
        this.level = level;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMediaUrls(ArrayList<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public PostResponse getPost() {
        return post;
    }

    public void setPost(PostResponse post) {
        this.post = post;
    }

    // Implement BindableContent
    @Override
    public Long getId() {
        return commentId;
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
}