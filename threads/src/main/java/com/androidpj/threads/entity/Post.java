package com.androidpj.threads.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // Nội dung bài viết

    @ElementCollection
    @CollectionTable(name = "post_media", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "media_url")
    private Set<String> mediaUrls;


    @Column(nullable = false)
    private String visibility; // Quyền riêng tư (public, private)

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Người đăng bài

}


