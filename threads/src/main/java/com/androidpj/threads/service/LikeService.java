package com.androidpj.threads.service;

import java.util.List;
import java.util.stream.Collectors;

import com.androidpj.threads.entity.Like;
import com.androidpj.threads.entity.Notification;
import com.androidpj.threads.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.LikeRepository;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    
    // Lấy tất cả bài viết mà người dùng đã thích
    public List<PostResponse> getPostsLikedByUser(Long userId) {
        List<Post> likedPosts = likeRepository.findLikedPostsByUserId(userId);
        return likedPosts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean likePost(Long postId, Long userId) {
        if (likeRepository.existsByPostPostIdAndUserUserId(postId, userId)) {
            return false; // Đã like rồi thì không làm gì cả
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        post.setLikeCount((post.getLikeCount() == null ? 0 : post.getLikeCount()) + 1);
        postRepository.save(post);

        return true;
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findByPostPostIdAndUserUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        likeRepository.delete(like);

        post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        postRepository.save(post);

        Notification notification = notificationRepository.findBySenderAndReceiverAndTypeAndPostId(
                user, post.getUser(), "like", postId);
        if (notification != null) {
            notificationRepository.delete(notification);
        }
    }

    public long countLikes(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return likeRepository.countByPost(post);
    }

    public boolean isPostLikedByUser(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        return likeRepository.existsByUserAndPost(user, post);
    }
}
