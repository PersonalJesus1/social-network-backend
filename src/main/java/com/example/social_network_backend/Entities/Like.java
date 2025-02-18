package com.example.social_network_backend.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    @ManyToOne
    @JoinColumn(name = "like_creator_user_id")
    private User likeCreator;

    private LocalDateTime likeDate;


    @PrePersist
    public void setDateBeforeInsert() {
        this.likeDate = LocalDateTime.now();
    }


}
