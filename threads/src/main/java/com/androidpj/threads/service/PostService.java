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

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	public List<Post> getAllPublicPosts() {
		return postRepository.findPublicPosts();
	}

//    public Page<Post> getPostsByUser(Long userId, Pageable pageable) {
//        Optional<User> user = userRepository.findById(userId);
//        if (user.isEmpty()) {
//            throw new RuntimeException("User not found");
//        }
//        return postRepository.findByUser(user.get(), pageable);
//    }

	public Post createPost(Post post, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		post.setUser(user);
		return postRepository.save(post);
	}

	public void deletePost(Long id) {
		postRepository.deleteById(id);
	}

//	public Page<Post> getAllPosts(int page, int size) {
//		Pageable pageable = PageRequest.of(page, size);
//		return postRepository.findAll(pageable);
//	}
//	public List<PostResponse> getAllPosts() {
//        List<Post> posts = postRepository.findAll();
//        return posts.stream().map(PostResponse::fromEntity).collect(Collectors.toList());
//    }
	
	public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAllWithImages();
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

}
