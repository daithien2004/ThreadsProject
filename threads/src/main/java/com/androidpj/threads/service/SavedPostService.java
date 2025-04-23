package com.androidpj.threads.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.SavedPost;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.SavedPostRepository;
import com.androidpj.threads.repository.UserRepository;

@Service
public class SavedPostService {
	@Autowired
	SavedPostRepository savedPostRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PostRepository postRepository;
	
	public void savePost(Long userId, Long postId) {
	    // Kiểm tra xem bài viết đã được lưu chưa
	    if (savedPostRepository.existsByUser_UserIdAndPost_PostId(userId, postId)) {
	        throw new RuntimeException("Bài viết đã được lưu.");
	    }

	    // Tiến hành lưu bài viết
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
	    Post post = postRepository.findById(postId)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));

	    SavedPost savedPost = new SavedPost();
	    savedPost.setUser(user);
	    savedPost.setPost(post);
	    
	    savedPostRepository.save(savedPost);
	}

    public void unsavePost(Long userId, Long postId) {
        SavedPost savedPost = savedPostRepository.findByUserUserIdAndPostPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("Bài viết chưa được lưu"));

        savedPostRepository.delete(savedPost);
    }
	public List<PostResponse> getSavedPostResponsesByUserId(Long userId) {
	    List<Post> savedPosts = savedPostRepository.findSavedPostsByUserId(userId);
	    return savedPosts.stream()
	            .map(PostResponse::new)
	            .collect(Collectors.toList());
	}

	public boolean isPostSavedByUser(Long userId, Long postId) {
	    return savedPostRepository.existsByUser_UserIdAndPost_PostId(userId, postId);
	}


}
