package com.example.theadsproject.listener;

import com.example.theadsproject.dto.NotificationResponse;

public interface NotificationListener {
    void onNotificationReceived(NotificationResponse notification);
}
