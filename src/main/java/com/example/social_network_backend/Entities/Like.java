package com.example.social_network_backend.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "like_creator_user_id")
    private User creator;

    @Column(nullable = false)
    private LocalDateTime date;

    @PrePersist
    public void setDateBeforeInsert() {
        this.date = LocalDateTime.now();
    }
}