package com.example.social_network_backend.Services;

import com.example.social_network_backend.Entities.Like;
import com.example.social_network_backend.Repositories.LikeRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public Like createLike(Like like) {
        likeRepository.save(like);
        return like;
    }

    public Like getLikeById(Long id) {
        return likeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Page<Like> getAllLikes(Pageable pageable) {
        return likeRepository.findAll(pageable);
    }

    @Transactional
    public void deleteLike(Long id) {
        likeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        likeRepository.deleteById(id);
    }

    public boolean existsByCreatorIdAndPostId(Long userId, Long postId) {
        return likeRepository.existsByCreatorIdAndPostId(userId, postId);
    }
}