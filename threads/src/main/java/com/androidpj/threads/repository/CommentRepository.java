package com.androidpj.threads.repository;

import com.androidpj.threads.entity.Comment;
import com.androidpj.threads.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    Comment findByCommentId(Long commentId);
    long countByPost(Post post);

    List<Comment> findByPostPostId(Long postId);
    List<Comment> findByParentCommentCommentId(Long parentId);
    List<Comment> findByPostPostIdAndParentCommentIsNull(Long postId); // Lấy comments gốc
    Optional<Comment> findByCommentIdAndUserUserId(Long commentId, Long userId);
    void deleteByCommentIdAndUserUserId(Long commentId, Long userId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment IS NULL ORDER BY c.createAt DESC")
    List<Comment> findRootCommentsByPostId(@Param("postId") Long postId);
}
