package com.androidpj.threads.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByUser(User user);

    @Query("SELECT p FROM Post p WHERE p.visibility = 'public' ORDER BY p.createdAt DESC")
    List<Post> findPublicPosts();
    
    Page<Post> findAll(Pageable pageable);
    
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.mediaUrls LEFT JOIN FETCH p.user WHERE p.visibility = 'public'")
    List<Post> findAllWithImages();
    
    boolean existsByPostIdAndUser_UserId(Long postId, Long userId); 

}
