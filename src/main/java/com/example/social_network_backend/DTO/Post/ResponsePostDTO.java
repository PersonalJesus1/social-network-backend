package com.example.social_network_backend.DTO.Post;

import com.example.social_network_backend.Entities.Image;

import java.time.LocalDateTime;

public record ResponsePostDTO(Long postId, String text, Image image, LocalDateTime createdDate, int likeCount) {
}