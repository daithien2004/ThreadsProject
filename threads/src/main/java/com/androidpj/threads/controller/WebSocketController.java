package com.androidpj.threads.controller;

import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.UserRepository;
import com.androidpj.threads.service.UserService;
import com.androidpj.threads.service.WebSocketService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Controller
public class WebSocketController {

    @Autowired
    WebSocketService webSocketService;

    @MessageMapping("/register")
    @Transactional
    public void registerUser(@Payload Long userId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = Objects.requireNonNull(headerAccessor.getSessionId(), "Session ID không thể null");
        webSocketService.registerUser(userId, sessionId);
    }

    @MessageMapping("/disconnect")
    @Transactional
    public void handleClientDisconnect(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = Objects.requireNonNull(headerAccessor.getSessionId(), "Session ID không thể null");
        webSocketService.handleClientDisconnect(sessionId);
    }
}