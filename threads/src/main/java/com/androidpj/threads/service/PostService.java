package com.androidpj.threads.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import com.androidpj.threads.dto.PostRequest;
import com.androidpj.threads.entity.Like;
import com.androidpj.threads.entity.Notification;
import com.androidpj.threads.repository.LikeRepository;
import com.androidpj.threads.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;
import com.androidpj.threads.repository.SavedPostRepository;
import com.androidpj.threads.repository.CommentRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private SavedPostRepository savedPostRepository;
	@Autowired
	private CommentRepository commentRepository;

	public boolean isUserOwnerOfPost(Long postId, Long userId) {
        return postRepository.existsByPostIdAndUser_UserId(postId, userId);
    }
	public List<Post> getAllPublicPosts() {
		return postRepository.findPublicPosts();
	}

	public PostResponse createPost(PostRequest postRequest) {
		if (postRequest.getUserId() == null) {
			throw new RuntimeException("User is required for creating a post");
		}

		User user = userRepository.findById(postRequest.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		// T?o Post t? request
		Post post = new Post();
		post.setContent(postRequest.getContent());
		post.setVisibility(postRequest.getVisibility());
		post.setMediaUrls(new HashSet<>(postRequest.getMediaUrls()));
		post.setUser(user);
		Post savedPost = postRepository.save(post);
		return new PostResponse(savedPost);
	}

	@Transactional
	public void deletePost(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new RuntimeException("Post not found"));
		
        // Xóa tất cả likes của bài viết
        likeRepository.deleteByPost(post);
        
        // Xóa tất cả lưu bài viết
        savedPostRepository.deleteByPost_PostId(postId);
        
        // Xóa tất cả comments của bài viết
        commentRepository.deleteByPost(post);
        
        // Cuối cùng xóa bài viết
        postRepository.delete(post);
	}

	public PostResponse getPostById(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new RuntimeException("Post not found"));
		return new PostResponse(post);
	}

	public List<PostResponse> getAllPosts() {
		List<Post> posts = postRepository.findAllWithImages();
		return posts.stream().map(PostResponse::new).collect(Collectors.toList());
	}
	
	public List<PostResponse> getPostsByUser(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    List<Post> posts = postRepository.findByUser(user);
	    return posts.stream().map(PostResponse::new).collect(Collectors.toList());
	}

	public List<PostResponse> getPostsFromFollowing(Long userId) {
        List<Post> posts = postRepository.findPostsByFollowing(userId);

        List<PostResponse> postResponses = posts.stream()
            .map(post -> new PostResponse(post)) 
            .collect(Collectors.toList());

        return postResponses;
    }

    public Map<String, Object> getPagedPosts(int page, int size) {
        org.springframework.data.domain.Pageable paging = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        
        org.springframework.data.domain.Page<Post> pageResult = postRepository.findAll(paging);
        
        List<PostResponse> posts = pageResult.getContent()
            .stream()
            .map(PostResponse::new)
            .collect(Collectors.toList());
            
        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        
        return response;
    }
}


