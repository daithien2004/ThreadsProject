package com.androidpj.threads.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.androidpj.threads.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    boolean existsByEmail(String email);
    User findByEmail(String email);
	User findByUserId(Long userId);
    User findBySessionId(String sessionId);
	// tìm kiếm user theo username hoặc nickname
    List<User> findByUsernameContainingIgnoreCaseOrNickNameContainingIgnoreCase(String username, String nickname);
    Optional<User> findById(Long userId);
    
}
