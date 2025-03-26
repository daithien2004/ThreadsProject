package com.androidpj.threads.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.androidpj.threads.dto.PostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	public List<Post> getAllPublicPosts() {
		return postRepository.findPublicPosts();
	}

	public PostResponse createPost(PostRequest postRequest) {
		if (postRequest.getUserId() == null) {
			throw new RuntimeException("User is required for creating a post");
		}

		// Tìm User t? userId
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

	 public void deletePost(Long postId) {
	        if (!postRepository.existsById(postId)) {
	            throw new RuntimeException("Post not found");
	        }
	        postRepository.deleteById(postId);
	    }

	public List<PostResponse> getAllPosts() {
		List<Post> posts = postRepository.findAllWithImages();
		return posts.stream().map(PostResponse::new).collect(Collectors.toList());
	}

}
