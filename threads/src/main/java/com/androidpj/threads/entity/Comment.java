package com.androidpj.threads.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    // Thêm trường để theo dõi comment cha
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    // Thêm trường để lưu các comment con
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> replies = new ArrayList<>();

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
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private Post post;

    @Column(name = "like_count")
    private Integer likeCount = 0;
}
