package com.example.theadsproject.handler;

import retrofit2.Callback;

public interface LikeHandler {
    void checkIfLiked(Long userId, Long itemId, Callback<Boolean> callback);
    void countLikes(Long itemId, Callback<Long> callback);
    void countComments(Long itemId, Callback<Long> callback);
    void like(Long userId, Long itemId, Callback<Void> callback);
    void unlike(Long userId, Long itemId, Callback<Void> callback);
}
