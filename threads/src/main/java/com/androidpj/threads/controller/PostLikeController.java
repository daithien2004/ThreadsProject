package com.androidpj.threads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.androidpj.threads.service.PostLikeService;

@RestController
@RequestMapping("/likes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestParam Long userId, @RequestParam Long postId) {
        postLikeService.likePost(userId, postId);
        return ResponseEntity.ok("Liked");
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<String> unlikePost(@RequestParam Long userId, @RequestParam Long postId) {
        postLikeService.unlikePost(userId, postId);
        return ResponseEntity.ok("Unliked");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countLikes(@RequestParam Long postId) {
        return ResponseEntity.ok(postLikeService.countLikes(postId));
    }

    @GetMapping("/is-liked")
    public ResponseEntity<Boolean> isLiked(@RequestParam Long userId, @RequestParam Long postId) {
        return ResponseEntity.ok(postLikeService.isPostLikedByUser(userId, postId));
    }
}
