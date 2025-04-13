package com.androidpj.threads.service;

import com.androidpj.threads.dto.CommentRequest;
import com.androidpj.threads.dto.CommentResponse;
import com.androidpj.threads.dto.PostRequest;
import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Comment;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.CommentRepository;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    public List<CommentResponse>  getCommentsByPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            List<Comment> commentList = commentRepository.findByPost(optionalPost.get());
            return commentList.stream().map(CommentResponse::new).collect(Collectors.toList());
        }
        return null;
    }

    public CommentResponse createPost(CommentRequest commentRequest) {
        if (commentRequest.getUserId() == null) {
            throw new RuntimeException("User is required for creating a comment");
        }

        if (commentRequest.getPostId() == null) {
            throw new RuntimeException("Post is required for creating a comment");
        }


        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setVisibility(commentRequest.getVisibility());
        comment.setMediaUrls(new HashSet<>(commentRequest.getMediaUrls()));
        comment.setUser(user);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponse(savedComment);
    }
}
