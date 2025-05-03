package com.androidpj.threads.service;

import com.androidpj.threads.dto.CommentRequest;
import com.androidpj.threads.dto.CommentResponse;
import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.dto.UserResponse;
import com.androidpj.threads.entity.Comment;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.CommentRepository;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    NotificationService notificationService;

//    public List<CommentResponse>  getCommentsByPost(Long postId) {
//        Optional<Post> optionalPost = postRepository.findById(postId);
//
//        if (optionalPost.isPresent()) {
//            List<Comment> commentList = commentRepository.findByPost(optionalPost.get());
//            return commentList.stream().map(CommentResponse::new).collect(Collectors.toList());
//        }
//        return null;
//    }
//
//    public CommentResponse createComment(CommentRequest commentRequest) {
//        if (commentRequest.getUserId() == null) {
//            throw new RuntimeException("User is required for creating a comment");
//        }
//
//        if (commentRequest.getPostId() == null) {
//            throw new RuntimeException("Post is required for creating a comment");
//        }
//
//
//        User user = userRepository.findById(commentRequest.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Post post = postRepository.findById(commentRequest.getPostId())
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//        Comment comment = new Comment();
//        comment.setContent(commentRequest.getContent());
//        comment.setVisibility(commentRequest.getVisibility());
//        comment.setMediaUrls(new HashSet<>(commentRequest.getMediaUrls()));
//        comment.setUser(user);
//        comment.setPost(post);
//        Comment savedComment = commentRepository.save(comment);
//        return new CommentResponse(savedComment);
//    }
//
    public CommentResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return new CommentResponse(comment);
    }

    public CommentResponse createComment(CommentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findByPostId(request.getPostId());

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setMediaUrls(new HashSet<>(request.getMediaUrls()));
        comment.setVisibility(request.getVisibility());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreateAt(LocalDateTime.now());

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findByCommentId(request.getParentCommentId());
            comment.setParentComment(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);

        return mapToCommentResponse(savedComment);
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findRootCommentsByPostId(postId);
        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getReplies(Long parentId) {
        List<Comment> replies = commentRepository.findByParentCommentCommentId(parentId);
        return replies.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, Long userId) {
        commentRepository.deleteByCommentIdAndUserUserId(commentId, userId);
    }

    public long countComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.countByPost(post);
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setContent(comment.getContent());
        response.setMediaUrls(comment.getMediaUrls() != null ? new ArrayList<>(comment.getMediaUrls()) : new ArrayList<>());
        response.setCreateAt(comment.getCreateAt());
        response.setVisibility(comment.getVisibility());
        response.setUser(new UserResponse(comment.getUser()));
        response.setPost(new PostResponse(comment.getPost()));

        // Xử lý nested comments
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            response.setReplies(comment.getReplies().stream()
                    .map(this::mapToCommentResponse)
                    .collect(Collectors.toList()));
        }

        // Tính độ sâu của comment
        int level = 0;
        Comment parent = comment.getParentComment();
        while (parent != null) {
            level++;
            parent = parent.getParentComment();
        }
        response.setLevel(level);

        return response;
    }
}
