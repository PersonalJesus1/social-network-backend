package com.example.social_network_backend.DTO.Message;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateMessageDTO(@NotNull(message = "Message text cannot be null")
                               @Size(max = 2000, message = "Message cannot exceed 2000 characters") String text) {
}