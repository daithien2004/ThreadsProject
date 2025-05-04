package com.androidpj.threads.controller;

import java.util.List;

import com.androidpj.threads.dto.CommentResponse;
import com.androidpj.threads.dto.NotificationRequest;
import com.androidpj.threads.dto.NotificationResponse;
import com.androidpj.threads.service.CommentService;
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
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    
    // Lấy danh sách bài viết mà người dùng đã thích
    @GetMapping("/user/{userId}")
    public List<PostResponse> getPostsLikedByUser(@PathVariable Long userId) {
        return likeService.getPostsLikedByUser(userId);
    }

    @PostMapping("/likePost")
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

    @PostMapping("/unlikePost")
    public ResponseEntity<?> unlikePost(@RequestParam Long userId, @RequestParam Long postId) {
        try {
            likeService.unlikePost(postId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not unlike post: " + e.getMessage());
        }
    }

    @PostMapping("/likeComment")
    public ResponseEntity<?> likeComment(@RequestParam Long userId, @RequestParam Long commentId) {
        try {
            // Like the post and check if a new like was created
            boolean isNewLike = likeService.likeComment(commentId, userId);

            if (!isNewLike) {
                // If no new like was created (post already liked), return success without sending notification
                return ResponseEntity.ok().body("Comment already liked");
            }

            // Lấy thông tin bài viết
            CommentResponse comment = commentService.getCommentById(commentId);
            Long receiverId = comment.getUser().getUserId();

            // Tạo yêu cầu thông báo
            NotificationRequest request = new NotificationRequest(receiverId, userId, "like", commentId);
            NotificationResponse response = notificationService.createNotification(request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not process like or send notification: " + e.getMessage());
        }
    }

    @PostMapping("/unlikeComment")
    public ResponseEntity<?> unlikeComment(@RequestParam Long userId, @RequestParam Long commentId) {
        try {
            likeService.unlikeComment(commentId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not unlike post: " + e.getMessage());
        }
    }


    @GetMapping("/postCount")
    public ResponseEntity<Long> countLikes(@RequestParam Long postId) {
        return ResponseEntity.ok(likeService.countPostLikes(postId));
    }

    @GetMapping("/commentCount")
    public ResponseEntity<Long> countCommentLikes(@RequestParam Long commentId) {
        return ResponseEntity.ok(likeService.countCommentLikes(commentId));
    }

    @GetMapping("/is-liked")
    public ResponseEntity<Boolean> isLiked(@RequestParam Long userId, @RequestParam Long postId) {
        return ResponseEntity.ok(likeService.isPostLikedByUser(userId, postId));
    }

    @GetMapping("comment/is-liked")
    public ResponseEntity<Boolean> isCommentLiked(@RequestParam Long userId, @RequestParam Long commentId) {
        return ResponseEntity.ok(likeService.isCommentLikedByUser(userId, commentId));
    }
}
