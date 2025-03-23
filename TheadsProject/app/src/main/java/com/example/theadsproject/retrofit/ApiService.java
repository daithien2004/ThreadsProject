package com.example.theadsproject.retrofit;

import com.example.theadsproject.DTO.PostResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("posts")
    Call<List<PostResponse>> getAllPosts();
}
