package com.androidpj.threads.config;

import com.androidpj.threads.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Các endpoint công khai (không cần đăng nhập)
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/activate",
                                "/posts",                    // GET all posts
                                "/posts/{postId}",           // GET post by ID
                                "/posts/user/{userId}",
                                "likes/postCount",
                                "likes/commentCount",
                                "comments/postCount",
                                "comments/commentCount",
                                "reposts/count/{postId}",
                                "comments/post/{postId}",
                                "comments/{id}",
                                "comments/replies/{parentId}",
                                "users/{userId}",
                                "follows/count/{userId}",
                                "reposts/my-reposts",
                                "users",
                                "reposts/my-reposts"
                        ).permitAll()

                        // Các endpoint yêu cầu xác thực
                        .requestMatchers(
                                "/posts/following",               // ví dụ: /posts/following (GET feed)
                                "/posts/{postId}/isOwner",        // kiểm tra quyền sở hữu bài viết
                                "/posts",                         // POST create post
                                "/posts/{postId}"                 // DELETE hoặc PUT bài viết
                        ).authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

