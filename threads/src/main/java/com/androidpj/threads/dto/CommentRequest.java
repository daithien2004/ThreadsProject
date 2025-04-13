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
}
