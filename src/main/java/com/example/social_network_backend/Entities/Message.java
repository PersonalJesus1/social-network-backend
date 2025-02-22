package com.example.social_network_backend.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    @NotNull(message = "Message text cannot be null")
    private String text;

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