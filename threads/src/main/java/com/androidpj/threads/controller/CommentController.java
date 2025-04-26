package com.androidpj.threads.controller;

import com.androidpj.threads.dto.*;
import com.androidpj.threads.service.CommentService;
import com.androidpj.threads.service.NotificationService;
import com.androidpj.threads.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createComment(commentRequest);

        PostResponse post = postService.getPostById(commentRequest.getPostId());

        NotificationRequest request = new NotificationRequest(post.getUser().getUserId(), commentRequest.getUserId(), "comment", commentRequest.getPostId());
        NotificationResponse response = notificationService.createNotification(request);

        return ResponseEntity.ok(commentResponse);
    }
}
