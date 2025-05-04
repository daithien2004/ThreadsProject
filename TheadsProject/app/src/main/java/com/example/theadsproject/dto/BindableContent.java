package com.example.theadsproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BindableContent {
    Long getId();
    String getContent();
    String getVisibility();
    ArrayList<String> getMediaUrls();
    UserResponse getUser();
    LocalDateTime getCreatedAt();
    Boolean getIsLoved();
    void setIsLoved(boolean loved);
}