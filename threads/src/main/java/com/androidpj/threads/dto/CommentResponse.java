package com.androidpj.threads.dto;

import com.androidpj.threads.UserResponse;
import com.androidpj.threads.entity.Comment;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

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
