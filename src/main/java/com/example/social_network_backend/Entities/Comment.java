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
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 2000, message = "Text of the post cannot exceed 2000 characters")
    @NotNull(message = "Text cannot be null")
    private String text;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_creator_user_id")
    private User creator;
    @PrePersist
    public void setDateBeforeInsert() {
        this.date = LocalDateTime.now();
    }
}