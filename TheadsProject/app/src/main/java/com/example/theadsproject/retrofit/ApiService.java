package com.example.theadsproject.retrofit;

import com.example.theadsproject.dto.CommentRequest;
import com.example.theadsproject.dto.CommentResponse;
import com.example.theadsproject.dto.OtpRequest;
import com.example.theadsproject.dto.PostRequest;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.UserRequest;
import com.example.theadsproject.dto.UserResponse;
import com.example.theadsproject.entity.User;

import java.util.List;
import java.util.Map;

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

    @GET("posts/{id}")
    Call<PostResponse> getPostById(@Path("id") Long postId);
    @GET("posts")
    Call<List<PostResponse>> getAllPosts();
    // Lay bài đăng của user
    @GET("posts/user/{userId}")
    Call<List<PostResponse>> getUserPosts(@Path("userId") Long userId);
    @GET("posts/following/{userId}")
    Call<List<PostResponse>> getPostsByFollowing(@Path("userId") Long userId);
    @GET("users/search")
    Call<List<UserResponse>> searchUsers(@Query("keyword") String keyword);
    @POST("follows")
    Call<Void> followUser(@Query("followerId") Long followerId, @Query("followingId") Long followingId);

    @DELETE("follows")
    Call<Void> unfollowUser(@Query("followerId") Long followerId, @Query("followingId") Long followingId);
    @GET("follows/users/{userId}/following")
    Call<List<UserResponse>> getFollowingUsers(@Path("userId") Long userId);

    @GET("users")
    Call<List<UserResponse>> getAllUsers();

    @POST("likes/like")
    Call<Void> likePost(@Query("userId") Long userId, @Query("postId") Long postId);

    @DELETE("likes/unlike")
    Call<Void> unlikePost(@Query("userId") Long userId, @Query("postId") Long postId);

    @GET("likes/count")
    Call<Long> countLikes(@Query("postId") Long postId);

    @GET("likes/is-liked")
    Call<Boolean> isPostLiked(@Query("userId") Long userId, @Query("postId") Long postId);


    @GET("comments/{postId}")
    Call<List<CommentResponse>> getPostComments(@Path("postId") Long postId);
    @POST("comments")
    Call<Void> createComment(@Body CommentRequest commentRequest);
    // Upload ảnh
    @Multipart
    @POST("upload")
    Call<List<String>> uploadImages(@Part List<MultipartBody.Part> images);
    // Tạo bài viết mới
    @POST("posts")
    Call<Void> createPost(@Body PostRequest postRequest);
    @GET("posts/{postId}/isOwner")
    Call<Boolean> isPostOwner(@Path("postId") Long postId, @Query("userId") Long userId);
    @POST("login")
    Call<UserResponse> checkLogin(@Body UserRequest userRequest);
    @POST("register")
    Call<Boolean> register(@Body UserRequest userRequest);
    @POST("activate")
    Call<Boolean> activate(@Body OtpRequest otpRequest);
    @POST("resetOtp")
    Call<Boolean> resetOtp(@Body UserRequest userRequest);
    @POST("resetPassword")
    Call<Boolean> resetPassword(@Body OtpRequest otpRequest);

}
