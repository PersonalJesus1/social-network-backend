package com.example.social_network_backend.DTO.Message;

import java.time.LocalDateTime;

public record ResponseMessageDTO(String text, LocalDateTime date, String creatorName,
                                 String receiverName) {
}