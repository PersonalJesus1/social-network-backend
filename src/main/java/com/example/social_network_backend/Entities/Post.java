package com.example.social_network_backend.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 2000, message = "Text of the post cannot exceed 2000 characters")
    @NotNull(message = "Text cannot be null")
    private String text;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @PrePersist
    public void setDateBeforeInsert() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void setDateAfterUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}