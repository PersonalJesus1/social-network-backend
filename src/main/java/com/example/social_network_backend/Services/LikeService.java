package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Like.CreateLikeDTO;
import com.example.social_network_backend.Entities.Like;
import com.example.social_network_backend.Entities.Post;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.LikeRepository;
import com.example.social_network_backend.Repositories.PostRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Like createLike(CreateLikeDTO dto) {
        Like like = mapToEntity(dto);
        like.setDate(LocalDateTime.now());
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
    public Like updateLike(Long id) {
        Like like = getLikeById(id);
        like.setDate(LocalDateTime.now());
        likeRepository.save(like);
        return like;
    }

    @Transactional
    public void deleteLike(Long id) {
        likeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        likeRepository.deleteById(id);
    }

    public boolean existsByCreatorIdAndPostId(Long userId, Long postId) {
        return likeRepository.existsByCreatorIdAndPostId(userId, postId);
    }

    public Like mapToEntity(CreateLikeDTO dto) {
        Post post = postRepository.findById(dto.postId()).orElseThrow(EntityNotFoundException::new);
        User creator = userRepository.findById(dto.userId()).orElseThrow(EntityNotFoundException::new);
        Like like = new Like();
        like.setPost(post);
        like.setCreator(creator);
        like.setDate(LocalDateTime.now());
        return like;
    }
}