package com.androidpj.threads.repository;

import com.androidpj.threads.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostPostIdAndUserUserId(Long postId, Long userId);
    void deleteByPostPostIdAndUserUserId(Long postId, Long userId);
}
