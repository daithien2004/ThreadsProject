package com.androidpj.threads.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.androidpj.threads.entity.Post;
import com.androidpj.threads.entity.SavedPost;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
	
    Optional<SavedPost> findByUserUserIdAndPostPostId(Long userId, Long postId);
    boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId);

	@Query("SELECT sp.post FROM SavedPost sp WHERE sp.user.userId = :userId")
	List<Post> findSavedPostsByUserId(@Param("userId") Long userId);

}

