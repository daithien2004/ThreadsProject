package com.androidpj.threads.DTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private ArrayList<String> mediaUrls;
    private String visibility;
    private LocalDateTime createdAt;
    private UserResponse user;


    public PostResponse(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.visibility = post.getVisibility();
        this.createdAt = post.getCreatedAt();
        this.user = new UserResponse(post.getUser()); 
        this.mediaUrls = new ArrayList<String>(post.getMediaUrls());
    }
}

