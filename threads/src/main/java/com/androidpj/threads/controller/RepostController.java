package com.androidpj.threads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.androidpj.threads.dto.RepostResponse;
import com.androidpj.threads.service.RepostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reposts")
public class RepostController {

    @Autowired
    private RepostService repostService;

    // Repost bài viết
    @PostMapping("/repost/{postId}")
    public ResponseEntity<?> repost(@PathVariable Long postId, @RequestParam String username) {
        repostService.repost(postId, username);
        return ResponseEntity.ok("Repost thành công!");
    }

    // Lấy các bài repost của người dùng
    @GetMapping("/my-reposts")
    public ResponseEntity<List<RepostResponse>> getMyReposts(@RequestParam Long userId) {
        List<RepostResponse> repostedPosts = repostService.getMyReposts(userId);
        return ResponseEntity.ok(repostedPosts);
    }
 // API đếm số lượng repost của một bài viết
    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> countReposts(@PathVariable Long postId) {
        Long repostCount = repostService.countReposts(postId);
        return ResponseEntity.ok(repostCount);
    }
    @GetMapping("/posts/{postId}/reposted/{username}")
    public ResponseEntity<Boolean> isPostReposted(@PathVariable Long postId, @PathVariable String username) {
        boolean alreadyReposted = repostService.hasUserReposted(postId, username);
        return ResponseEntity.ok(alreadyReposted);
    }



}

