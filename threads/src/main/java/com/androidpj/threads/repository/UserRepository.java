package com.androidpj.threads.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.androidpj.threads.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
