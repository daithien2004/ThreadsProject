package com.androidpj.threads.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.service.SavedPostService;

@RestController
@RequestMapping("/saved")
public class SavedPostController {

    @Autowired
    private SavedPostService savedPostService;
    
    @PostMapping("/save")
    public ResponseEntity<String> savePost(@RequestParam Long userId, @RequestParam Long postId) {
        savedPostService.savePost(userId, postId);
        return ResponseEntity.ok("Đã lưu bài viết.");
    }

    @DeleteMapping("/unsave")
    public ResponseEntity<String> unsavePost(@RequestParam Long userId, @RequestParam Long postId) {
        savedPostService.unsavePost(userId, postId);
        return ResponseEntity.ok("Đã bỏ lưu bài viết.");
    }
    
 // Lấy danh sách bài viết mà người dùng đã thích
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getSavedPosts(@PathVariable Long userId) {
        List<PostResponse> savedPosts = savedPostService.getSavedPostResponsesByUserId(userId);
        return ResponseEntity.ok(savedPosts);
    }
    
    @GetMapping("/posts/{postId}/saved-by/{userId}")
    public ResponseEntity<Boolean> isPostSaved(@PathVariable Long userId, @PathVariable Long postId) {
        boolean isSaved = savedPostService.isPostSavedByUser(userId, postId);
        return ResponseEntity.ok(isSaved);
    }


}
