package com.example.social_network_backend.DTO.Comment;

import java.time.LocalDateTime;

public record ResponseCommentDTO(Long commentId, String text, LocalDateTime createdDate){
}