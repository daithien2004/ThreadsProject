package com.androidpj.threads.controller;

import com.androidpj.threads.dto.CommentRequest;
import com.androidpj.threads.dto.CommentResponse;
import com.androidpj.threads.dto.PostRequest;
import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createPost(commentRequest);
        return ResponseEntity.ok(commentResponse);
    }
}
