package com.example.social_network_backend.DTO.Comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentDTO(
        @NotNull(message = "Text cannot be null") @Size(max = 2000, message = "Text of the post cannot exceed 2000 characters") String text,
        @NotNull Long postId,
        @NotNull Long userId) {
}