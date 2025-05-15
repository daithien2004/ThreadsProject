package com.example.theadsproject.retrofit;

import com.example.theadsproject.dto.CommentRequest;
import com.example.theadsproject.dto.CommentResponse;
//import com.example.theadsproject.dto.NotificationResponse;
import com.example.theadsproject.dto.NotificationResponse;
import com.example.theadsproject.dto.OtpRequest;
import com.example.theadsproject.dto.PostRequest;
import com.example.theadsproject.dto.PostResponse;
import com.example.theadsproject.dto.RepostResponse;
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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") Long postId, @Query("userId") Long userId);

    @DELETE("comments/{id}")
    Call<Void> deleteComment(@Path("id") Long commentId, @Query("userId") Long userId);

    @GET("posts/{id}")
    Call<PostResponse> getPostById(@Path("id") Long postId);

    @GET("posts")
    Call<List<PostResponse>> getAllPosts();

    @GET("posts/page")
    Call<List<PostResponse>> getPostsByPage(
        @Query("page") int page,
        @Query("size") int size
    );

    // Lay bài đăng của user
    @GET("posts/user/{userId}")
    Call<List<PostResponse>> getUserPosts(@Path("userId") Long userId);

    @GET("posts/following/{userId}")
    Call<List<PostResponse>> getPostsByFollowing(@Path("userId") Long userId);

    @GET("users/search")
    Call<List<UserResponse>> searchUsers(@Query("keyword") String keyword);
    @PUT("{id}/bio")
    Call<Void> updateBio(@Path("id") Long userId, @Body Map<String, String> body);
    @POST("follows")
    Call<Void> followUser(@Query("followerId") Long followerId, @Query("followingId") Long followingId);

    @DELETE("follows")
    Call<Void> unfollowUser(@Query("followerId") Long followerId, @Query("followingId") Long followingId);

    @GET("follows/users/{userId}/following")
    Call<List<UserResponse>> getFollowingUsers(@Path("userId") Long userId);

    @GET("follows/count/{userId}")
    Call<Long> getFollowerCount(@Path("userId") Long userId);

    @GET("users")
    Call<List<UserResponse>> getAllUsers();
    @PUT("users/{id}")
    Call<Void> updateUser(@Path("id") Long userId, @Body UserRequest userRequest);


    @POST("likes/likePost")
    Call<Void> likePost(@Query("userId") Long userId, @Query("postId") Long postId);

    @POST("likes/unlikePost")
    Call<Void> unlikePost(@Query("userId") Long userId, @Query("postId") Long postId);

    @POST("likes/likeComment")
    Call<Void> likeComment(@Query("userId") Long userId, @Query("commentId") Long commentId);

    @POST("likes/unlikeComment")
    Call<Void> unlikeComment(@Query("userId") Long userId, @Query("commentId") Long commentId);

    @GET("likes/postCount")
    Call<Long> countPostLikes(@Query("postId") Long postId);

    @GET("likes/commentCount")
    Call<Long> countCommentLikes(@Query("commentId") Long commentId);

    @GET("likes/is-liked")
    Call<Boolean> isPostLiked(@Query("userId") Long userId, @Query("postId") Long postId);

    @GET("likes/comment/is-liked")
    Call<Boolean> isCommentLiked(@Query("userId") Long userId, @Query("commentId") Long commentId);

    @GET("likes/user/{userId}")
    Call<List<PostResponse>> getLikedPostsByUser(@Path("userId") Long userId);

    @POST("saved/save")
    Call<Void> savePost(@Query("userId") Long userId, @Query("postId") Long postId);

    @DELETE("saved/unsave")
    Call<Void> unsavePost(@Query("userId") Long userId, @Query("postId") Long postId);

    @GET("saved/user/{userId}")
    Call<List<PostResponse>> getSavedPostsByUser(@Path("userId") Long userId);

    @GET("saved/posts/{postId}/saved-by/{userId}")
    Call<Boolean> isPostSaved(@Path("userId") Long userId, @Path("postId") Long postId);

    @GET("comments/post/{postId}")
    Call<List<CommentResponse>> getPostComments(@Path("postId") Long postId);

    @POST("comments")
    Call<Void> createComment(@Body CommentRequest commentRequest);

    @GET("comments/postCount")
    Call<Long> countPostComments(@Query("postId") Long postId);

    @GET("comments/commentCount")
    Call<Long> countCommentComments(@Query("commentId") Long commentId);

    @GET("comments/{id}")
    Call<CommentResponse> getCommentById(@Path("id") Long commentId);

    @GET("comments/replies/{parentId}")
    Call<List<CommentResponse>> getCommentReplies(@Path("parentId") Long postId);

    // Upload ảnh
    @Multipart
    @POST("upload")
    Call<List<String>> uploadImages(@Part List<MultipartBody.Part> images);

    // Tạo bài viết mới
    @POST("posts")
    Call<Void> createPost(@Body PostRequest postRequest);

    @GET("posts/{postId}/isOwner")
    Call<Boolean> isPostOwner(@Path("postId") Long postId, @Query("userId") Long userId);

    @GET("comments/{commentId}/isOwner")
    Call<Boolean> isCommentOwner(@Path("commentId") Long commentId, @Query("userId") Long userId);

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

	@GET("notifications/{userId}")
    Call<List<NotificationResponse>> getUserNotifications(@Path("userId") Long userId);

    @GET("reposts/my-reposts")
    Call<List<RepostResponse>> getMyReposts(@Query("userId") Long userId);

    @POST("reposts/repost/{postId}")
    Call<Void> repost(
            @Path("postId") Long postId,
            @Query("username") String username
    );
    @GET("reposts/count/{postId}")
    Call<Long> countReposts(@Path("postId") Long postId);
    @GET("reposts/posts/{postId}/reposted/{username}")
    Call<Boolean> isPostReposted(@Path("postId") Long postId, @Path("username") String username);



    // GET thông tin user bất kỳ
    @GET("users/{userId}")
    Call<UserResponse> getUserById(@Path("userId") Long userId);

    @GET("posts/paged")
    Call<Map<String, Object>> getPagedPosts(
        @Query("page") int page,
        @Query("size") int size
    );

}