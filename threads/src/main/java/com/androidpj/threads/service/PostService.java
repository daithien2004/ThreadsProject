package com.androidpj.threads.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.androidpj.threads.DTO.PostResponse;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	public List<Post> getAllPublicPosts() {
		return postRepository.findPublicPosts();
	}

	public PostResponse createPost(Post post) {
		if (post.getUser() == null || post.getUser().getUser_id() == null) { 
			throw new RuntimeException("User is required for creating a post");
		}
		User user = userRepository.findById(post.getUser().getUser_id())
				.orElseThrow(() -> new RuntimeException("User not found"));
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
