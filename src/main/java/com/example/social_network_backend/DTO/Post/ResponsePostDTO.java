package com.example.social_network_backend.DTO.Post;

import java.time.LocalDateTime;

public record ResponsePostDTO(Long postId, String text, String picture, LocalDateTime date, int likeCount) {
}