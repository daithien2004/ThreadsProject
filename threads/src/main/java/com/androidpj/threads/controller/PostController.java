package com.androidpj.threads.controller;

import com.androidpj.threads.dto.PostRequest;
import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
//    @GetMapping
//    public List<PostResponse> getAllPosts() {
//        return postService.getAllPosts();
//    }

//    // Lấy danh sách bài đăng của một user cụ thể
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(postService.getPostsByUser(userId));
//    }


    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        PostResponse postResponse = postService.createPost(postRequest);
        return ResponseEntity.ok(postResponse);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }

}
