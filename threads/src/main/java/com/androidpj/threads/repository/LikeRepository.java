package com.androidpj.threads.repository;

import java.util.List;
import java.util.Optional;

import com.androidpj.threads.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    long countByPost(Post post);
    Optional<Like> findByUserUserIdAndPostPostId(Long userId, Long postId);
    
    @Query("SELECT l.post FROM Like l WHERE l.user.userId = :userId")
    List<Post> findLikedPostsByUserId(Long userId);

    boolean existsByPostPostIdAndUserUserId(Long postId, Long userId);
    void deleteByPostPostIdAndUserUserId(Long postId, Long userId);
    Optional<Like> findByPostPostIdAndUserUserId(Long postId, Long userId);
}

