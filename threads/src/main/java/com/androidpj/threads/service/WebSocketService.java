package com.androidpj.threads.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.androidpj.threads.dto.NotificationResponse;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void registerUser(Long userId, String sessionId) {
        if (userId == null || sessionId == null) {
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/user/queue/errors",
                    "Invalid userId or session"
            );
            return;
        }

        userRepository.findById(userId)
                .map(user -> {
                    user.setSessionId(sessionId);
                    return userRepository.save(user);
                })
                .ifPresentOrElse(
                        user -> messagingTemplate.convertAndSendToUser(
                                sessionId,
                                "/user/queue/register",
                                "User " + userId + " registered successfully"
                        ),
                        () -> messagingTemplate.convertAndSendToUser(
                                sessionId,
                                "/user/queue/errors",
                                "User " + userId + " not found"
                        )
                );
    }

    public void handleClientDisconnect(String sessionId) {
        User user = userRepository.findBySessionId(sessionId);
        if (user != null) {
            user.setSessionId(null);
            userRepository.save(user);
        };
    }

    public void sendNotification(Long receiverId, NotificationResponse notification) {
        if (receiverId == null || notification == null) {
            throw new IllegalArgumentException("Receiver ID và notification không được null");
        }

        userRepository.findById(receiverId)
                .filter(user -> user.getSessionId() != null)
                .ifPresentOrElse(
                        user -> messagingTemplate.convertAndSend("/topic/notifications/" + receiverId, notification),
                        () -> System.out.println("Không tìm thấy người dùng {} hoặc người dùng không kết nối" + receiverId)
                );
    }
}