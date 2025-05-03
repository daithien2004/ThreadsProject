package com.androidpj.threads.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.Repost;
import com.androidpj.threads.entity.User;

@Repository
public interface RepostRepository extends JpaRepository<Repost, Long> {
    List<Repost> findByUser(User user);

    boolean existsByUserAndPost(User user, Post post);
    Long countByPostPostId(Long postId);

}

