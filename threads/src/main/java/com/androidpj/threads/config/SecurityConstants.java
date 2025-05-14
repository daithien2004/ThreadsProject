package com.androidpj.threads.config;

public class SecurityConstants {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/login",
            "/register",
            "/activate",
            "/resetOtp",
            "/resetPassword",
            "/posts",
            "/posts/{postId}",
            "/posts/user/{userId}",
            "/likes/postCount",
            "/likes/commentCount",
            "/comments/postCount",
            "/comments/commentCount",
            "/reposts/count/{postId}",
            "/comments/post/{postId}",
            "/comments/{id}",
            "/comments/replies/{parentId}",
            "/users/{userId}",
            "/follows/count/{userId}",
            "/reposts/my-reposts",
            "/users",
            "/reposts/my-reposts",
            "/likes/is-liked",
            "/likes/comment/is-liked"
    };
}