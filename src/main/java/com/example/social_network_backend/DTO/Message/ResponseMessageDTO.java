package com.example.social_network_backend.DTO.Message;

import java.time.LocalDateTime;

public record ResponseMessageDTO(Long id, String text, LocalDateTime createdDate, String creatorName,
                                 String receiverName) {
}