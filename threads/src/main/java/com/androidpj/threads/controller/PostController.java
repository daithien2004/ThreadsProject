package com.androidpj.threads.controller;

import com.androidpj.threads.DTO.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    // Tạo bài đăng mới
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam Long userId) {
        Post createdPost = postService.createPost(post, userId);
        return ResponseEntity.ok(createdPost);
    }


    // Xóa bài đăng
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Bài đăng đã được xóa thành công!");
    }
}
