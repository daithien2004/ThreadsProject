package com.androidpj.threads.service;

import com.androidpj.threads.dto.CommentRequest;
import com.androidpj.threads.dto.CommentResponse;
import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.dto.UserResponse;
import com.androidpj.threads.entity.Comment;
import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.CommentRepository;
import com.androidpj.threads.repository.NotificationRepository;
import com.androidpj.threads.repository.PostRepository;
import com.androidpj.threads.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Autowired
    private NotificationRepository notificationRepository;

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

    public long countPostComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.countByPost(post);
    }

    public long countCommentComments(Long postId) {
        Comment comment = commentRepository.findById(postId).orElseThrow();
        return commentRepository.countByParentCommentCommentId(comment.getCommentId());
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setContent(comment.getContent());
        response.setMediaUrls(comment.getMediaUrls() != null ? new ArrayList<>(comment.getMediaUrls()) : new ArrayList<>());
        response.setCreatedAt(comment.getCreateAt());
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

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Post not found");
        }
        commentRepository.deleteById(commentId);
    }

    public boolean isUserOwnerOfComment(Long commentId, Long userId) {
        return commentRepository.existsByCommentIdAndUser_UserId(commentId, userId);
    }
}
