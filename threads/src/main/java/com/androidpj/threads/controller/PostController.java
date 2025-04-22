package com.androidpj.threads.controller;

import com.androidpj.threads.dto.NotificationRequest;
import com.androidpj.threads.dto.NotificationResponse;
import com.androidpj.threads.dto.PostRequest;
import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.service.NotificationService;
import com.androidpj.threads.service.PostService;
import com.androidpj.threads.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{postId}/isOwner")
    public ResponseEntity<Boolean> isPostOwner(@PathVariable Long postId, @RequestParam Long userId) {
        boolean isOwner = postService.isUserOwnerOfPost(postId, userId);
        return ResponseEntity.ok(isOwner);
    }

//    // Lấy danh sách bài đăng của một user cụ thể
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(postService.getPostsByUser(userId));
//    }

    // lấy bài đăng của 1 user nhất định
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId) {
        List<PostResponse> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }

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

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        PostResponse postResponse = postService.getPostById(postId);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        postService.likePost(postId, userId);

        PostResponse post = postService.getPostById(postId);
        Long receiverId = post.getUser().getUserId();

        User sender = userService.getUserById(userId);
        NotificationRequest request = new NotificationRequest(receiverId, userId, "like", postId);
        NotificationResponse notification = notificationService.createNotification(request);

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("receiverId", receiverId);
        notificationData.put("senderId", userId);
        notificationData.put("type", "like");
        notificationData.put("postId", postId);
        notificationData.put("senderName", sender.getUsername());
        notificationData.put("senderAvatar", sender.getImage());
        notificationData.put("createdAt", notification.getCreatedAt());

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity("http://localhost:3000/emit-notification", notificationData, String.class);
        } catch (RestClientException e) {
            // Logging error or handling failure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not send notification");
        }

        return ResponseEntity.ok(notificationData);
    }
}
