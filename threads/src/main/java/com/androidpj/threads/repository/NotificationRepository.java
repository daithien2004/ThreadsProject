package com.androidpj.threads.repository;

import com.androidpj.threads.entity.Notification;
import com.androidpj.threads.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Tìm tất cả thông báo gửi đến một người dùng cụ thể
    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);

    // Tuỳ chọn: đánh dấu đã đọc
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiver = :receiver")
    void markAllAsReadByReceiver(@Param("receiver") User receiver);

    Notification findBySenderAndReceiverAndTypeAndPostId(User sender, User receiver, String type, Long postId);
    Notification findBySenderUserIdAndReceiverUserIdAndType(Long senderId, Long receiverId, String type);
    Notification findByPostId(Long postId);
}

