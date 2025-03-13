package com.example.theadsproject.entity;

public class Post {
    private String username;
    private String time;
    private String content;
    private String imageUrl; // Nếu null => Không có ảnh
    private String avatar;
    public Post(String username, String time, String content, String imageUrl, String avatar) {
        this.username = username;
        this.time = time;
        this.content = content;
        this.imageUrl = imageUrl;
        this.avatar = avatar;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getUsername() { return username; }
    public String getTime() { return time; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
}

