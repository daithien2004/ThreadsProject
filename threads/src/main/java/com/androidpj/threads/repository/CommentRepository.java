package com.androidpj.threads.repository;

import com.androidpj.threads.entity.Comment;
import com.androidpj.threads.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
