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
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 2000, message = "Text of the post cannot exceed 2000 characters")
    @NotNull(message = "Text cannot be null")
    private String text;

    @Column(nullable = false)
    private LocalDateTime date;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> postLikeList;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> postCommentList;

    @PrePersist
    public void setDateBeforeInsert() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
    }
}