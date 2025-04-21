package com.example.social_network_backend.DTO.Like;

import jakarta.validation.constraints.NotNull;

public record CreateLikeDTO(
        @NotNull Long postId,
        @NotNull Long userId) {
}