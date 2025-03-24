package com.example.theadsproject.retrofit;

import com.example.theadsproject.DTO.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int postId);
    @GET("posts")
    Call<List<PostResponse>> getAllPosts();
}
