package com.example.social_network_backend.DTO.Like;

import java.time.LocalDateTime;

public record ResponseLikeDTO(LocalDateTime createdDate, Long likeId) {
}