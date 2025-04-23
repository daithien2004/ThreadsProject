package com.androidpj.threads.service;

import com.androidpj.threads.dto.NotificationRequest;
import com.androidpj.threads.dto.NotificationResponse;
import com.androidpj.threads.dto.PostResponse;
import com.androidpj.threads.entity.Notification;
import com.androidpj.threads.entity.User;
import com.androidpj.threads.repository.NotificationRepository;
import com.androidpj.threads.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String SOCKET_SERVER_URL = "http://localhost:3000/emit-notification";

    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notis = notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver);

        return notis.stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        // Validate users
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification notification = new Notification(sender, receiver, request.getType(), request.getPostId(), false);

        Notification savedNotification = notificationRepository.save(notification);

        return new NotificationResponse(savedNotification);
    }

}
