package com.androidpj.threads.controller;

import java.util.List;

import com.androidpj.threads.dto.NotificationRequest;
import com.androidpj.threads.dto.NotificationResponse;
import com.androidpj.threads.service.NotificationService;
import com.androidpj.threads.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.service.LikeService;

@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;
    
    // Lấy danh sách bài viết mà người dùng đã thích
    @GetMapping("/user/{userId}")
    public List<PostResponse> getPostsLikedByUser(@PathVariable Long userId) {
        return likeService.getPostsLikedByUser(userId);
    }

    @PostMapping("/like")
    public ResponseEntity<?> likePost(@RequestParam Long userId, @RequestParam Long postId) {
        try {
            // Like the post and check if a new like was created
            boolean isNewLike = likeService.likePost(postId, userId);

            if (!isNewLike) {
                // If no new like was created (post already liked), return success without sending notification
                return ResponseEntity.ok().body("Post already liked");
            }

            // Lấy thông tin bài viết
            PostResponse post = postService.getPostById(postId);
            Long receiverId = post.getUser().getUserId();

            // Tạo yêu cầu thông báo
            NotificationRequest request = new NotificationRequest(receiverId, userId, "like", postId);
            NotificationResponse response = notificationService.createNotification(request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not process like or send notification: " + e.getMessage());
        }
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> unlikePost(@RequestParam Long userId, @RequestParam Long postId) {
        try {
            likeService.unlikePost(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not unlike post: " + e.getMessage());
        }
    }


    @GetMapping("/count")
    public ResponseEntity<Long> countLikes(@RequestParam Long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }

    @GetMapping("/is-liked")
    public ResponseEntity<Boolean> isLiked(@RequestParam Long userId, @RequestParam Long postId) {
        return ResponseEntity.ok(likeService.isPostLikedByUser(userId, postId));
    }
}
