package com.example.social_network_backend.DTO.Comment;

import java.time.LocalDateTime;

public record ResponseUpdatedCommentDTO(Long commentId, String text, LocalDateTime updatedDate) {
}