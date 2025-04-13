package com.androidpj.threads.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.androidpj.threads.entity.Follow;
import com.androidpj.threads.entity.User;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	boolean existsByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);

    void deleteByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);
    
    @Query("SELECT f.following FROM Follow f WHERE f.follower.userId = :userId")
    List<User> findFollowingByUserId(@Param("userId") Long userId);
}
