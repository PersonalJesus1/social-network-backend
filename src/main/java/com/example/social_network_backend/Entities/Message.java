package com.example.social_network_backend.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "message_creator_user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "message_receiver_user_id")
    private User receiver;

    @PrePersist
    public void setDateBeforeInsert() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void setDateAfterUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}