package com.androidpj.threads.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến user gửi thông báo
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    // Liên kết đến user nhận thông báo
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    private String type;

    private Long postId;

    private Boolean isRead = false;

    @CreationTimestamp
    private Timestamp createdAt;

    public Notification(User sender, User receiver, String type, Long postId, Boolean isRead) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.postId = postId;
        this.isRead = isRead;
    }
}


