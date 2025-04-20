package com.androidpj.threads.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.PostLike;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.PostLikeRepository;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;

@Service
public class PostLikeService {
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    
 // Lấy tất cả bài viết mà người dùng đã thích
    public List<PostResponse> getPostsLikedByUser(Long userId) {
        List<Post> likedPosts = postLikeRepository.findLikedPostsByUserId(userId);
        return likedPosts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

//    // Kiểm tra người dùng đã thích bài viết chưa
//    public boolean userLikedPost(Long userId, Long postId) {
//        return postLikeRepository.existsByUserIdAndPostId(userId, postId);
//    }

    public void likePost(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        if (!postLikeRepository.existsByUserAndPost(user, post)) {
            PostLike like = new PostLike();
            like.setUser(user);
            like.setPost(post);
            postLikeRepository.save(like);
        }
    }

    public void unlikePost(Long userId, Long postId) {
        PostLike like = postLikeRepository.findByUserUserIdAndPostPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("Like not found")); // <-- có thể gây lỗi 500
        postLikeRepository.delete(like);
    }

    public long countLikes(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return postLikeRepository.countByPost(post);
    }

    public boolean isPostLikedByUser(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        return postLikeRepository.existsByUserAndPost(user, post);
    }
}
