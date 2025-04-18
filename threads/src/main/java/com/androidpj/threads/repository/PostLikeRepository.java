package com.androidpj.threads.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.PostLike;
import com.androidpj.threads.entity.User;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    long countByPost(Post post);
    Optional<PostLike> findByUserUserIdAndPostPostId(Long userId, Long postId);

}

