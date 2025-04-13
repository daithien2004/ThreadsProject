package com.androidpj.threads.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    @ElementCollection
    @CollectionTable(name = "comment_media", joinColumns = @JoinColumn(name = "comment_id"))
    @Column(name = "media_url")
    private Set<String> mediaUrls;

    @CreationTimestamp
    @Column(name = "create_at", unique = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private String visibility; // Quyền riêng tư (public, private)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
