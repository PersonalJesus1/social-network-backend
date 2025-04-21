package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}