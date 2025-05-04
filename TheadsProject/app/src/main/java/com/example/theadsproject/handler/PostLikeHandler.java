package com.example.theadsproject.handler;

import com.example.theadsproject.retrofit.ApiService;
import com.example.theadsproject.retrofit.RetrofitClient;

import retrofit2.Callback;

public class PostLikeHandler implements LikeHandler {
    private final ApiService api = RetrofitClient.getApiService();

    @Override
    public void checkIfLiked(Long userId, Long itemId, Callback<Boolean> callback) {
        api.isPostLiked(userId, itemId).enqueue(callback);
    }

    @Override
    public void countLikes(Long itemId, Callback<Long> callback) {
        api.countPostLikes(itemId).enqueue(callback);
    }

    @Override
    public void countComments(Long itemId, Callback<Long> callback) {
        api.countPostComments(itemId).enqueue(callback);
    }

    @Override
    public void like(Long userId, Long itemId, Callback<Void> callback) {
        api.likePost(userId, itemId).enqueue(callback);
    }

    @Override
    public void unlike(Long userId, Long itemId, Callback<Void> callback) {
        api.unlikePost(userId, itemId).enqueue(callback);
    }
}
