package com.example.social_network_backend.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Size(max = 2000, message = "Text of the post cannot exceed 2000 characters")
    @NotNull(message = "Text cannot be null")
    private String postText;

    private String postPicture;

    private LocalDateTime postDate;

    @ManyToOne
    @JoinColumn(name = "post_creator_user_id")
    private User postCreator;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Like> postLikeList;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> postCommentList;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(String postPicture) {
        this.postPicture = postPicture;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public User getPostCreator() {
        return postCreator;
    }

    public void setPostCreator(User postCreator) {
        this.postCreator = postCreator;
    }

    public List<Like> getPostLikeList() {
        return postLikeList;
    }

    public void setPostLikeList(List<Like> postLikeList) {
        this.postLikeList = postLikeList;
    }

    public List<Comment> getPostCommentList() {
        return postCommentList;
    }

    public void setPostCommentList(List<Comment> postCommentList) {
        this.postCommentList = postCommentList;
    }
}
