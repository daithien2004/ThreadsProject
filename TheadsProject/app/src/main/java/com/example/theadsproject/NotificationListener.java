package com.example.theadsproject;

import com.example.theadsproject.dto.NotificationResponse;

// NotificationListener.java
public interface NotificationListener {
    void onNotificationReceived(NotificationResponse notification);
}
