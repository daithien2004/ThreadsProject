package com.androidpj.threads.dto;

import com.androidpj.threads.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long commentId;
    String content;
    private ArrayList<String> mediaUrls;
    private LocalDateTime createAt;
    private String visibility;
    private UserResponse user;
    private PostResponse post;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.mediaUrls = new ArrayList<>(comment.getMediaUrls());
        this.createAt = comment.getCreateAt();
        this.visibility = comment.getVisibility();
        this.user = new UserResponse(comment.getUser());
        this.post = new PostResponse(comment.getPost());
    }
}
