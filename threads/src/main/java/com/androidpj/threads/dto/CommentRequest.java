package com.androidpj.threads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private String content;
    private ArrayList<String> mediaUrls;
    private String visibility;
    private Long userId;
    private Long postId;
    // Thêm trường parentId để xác định comment cha
    private Long parentCommentId;

    public CommentRequest(String content, ArrayList<String> mediaUrls, String visibility, Long userId, Long postId) {
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.visibility = visibility;
        this.userId = userId;
        this.postId = postId;
    }
}
