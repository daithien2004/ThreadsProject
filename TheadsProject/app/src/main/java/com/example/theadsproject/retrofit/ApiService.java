package com.example.theadsproject.retrofit;

import com.example.theadsproject.dto.PostRequest;
import com.example.theadsproject.dto.PostResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") Long postId);
    @GET("posts")
    Call<List<PostResponse>> getAllPosts();

//    lay bài đăng của user
    @GET("posts/user/{userId}")
    Call<List<PostResponse>> getUserPosts(@Path("userId") Long userId);


    // Upload ảnh
    @Multipart
    @POST("upload")
    Call<List<String>> uploadImages(@Part List<MultipartBody.Part> images);

    // Tạo bài viết mới
    @POST("posts")
    Call<Void> createPost(@Body PostRequest postRequest);

    @GET("posts/{postId}/isOwner")
    Call<Boolean> isPostOwner(@Path("postId") Long postId, @Query("userId") Long userId);

}
