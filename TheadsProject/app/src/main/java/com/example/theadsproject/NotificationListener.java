package com.example.theadsproject;

import com.example.theadsproject.dto.NotificationResponse;

public interface NotificationListener {
    void onNotificationReceived(NotificationResponse notification);
}
