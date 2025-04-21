package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCreator(User user, Pageable pageable);
    Post getPostById(@NotNull Long aLong);
}