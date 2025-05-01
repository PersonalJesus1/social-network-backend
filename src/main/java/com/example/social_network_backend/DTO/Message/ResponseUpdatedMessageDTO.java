package com.example.social_network_backend.DTO.Message;

import java.time.LocalDateTime;

public record ResponseUpdatedMessageDTO(Long id, String text, LocalDateTime updatedDate, String creatorName,
                                        String receiverName) {
}