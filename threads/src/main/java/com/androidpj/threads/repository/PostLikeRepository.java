package com.androidpj.threads.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.PostLike;
import com.androidpj.threads.entity.User;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    long countByPost(Post post);
    Optional<PostLike> findByUserUserIdAndPostPostId(Long userId, Long postId);
    
    @Query("SELECT pl.post FROM PostLike pl WHERE pl.user.userId = :userId")
    List<Post> findLikedPostsByUserId(Long userId);
}

