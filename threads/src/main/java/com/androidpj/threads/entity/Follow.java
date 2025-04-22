package com.androidpj.threads.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "follow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    // Người theo dõi
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    // Người được theo dõi
    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

}
