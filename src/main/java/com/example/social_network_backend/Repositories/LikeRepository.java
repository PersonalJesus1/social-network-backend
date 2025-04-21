package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByCreatorIdAndPostId(Long creatorId, Long postId);
}