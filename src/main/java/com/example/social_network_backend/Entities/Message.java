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
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "message_creator_user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "message_receiver_user_id")
    private User receiver;

    @PrePersist
    @PreUpdate
    public void setDateBeforeInsertOrUpdate() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }
}