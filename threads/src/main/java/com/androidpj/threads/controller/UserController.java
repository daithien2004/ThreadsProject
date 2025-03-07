package com.androidpj.threads.controller;

import com.androidpj.threads.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {
    @GetMapping("")
    public List<String> getUsers() {
        return Arrays.asList("User1", "User2", "User3");
    }
}
