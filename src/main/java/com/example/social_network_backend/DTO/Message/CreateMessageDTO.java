package com.example.social_network_backend.DTO.Message;

public record CreateMessageDTO(String messageText, Long creatorId, Long receiverId) {

}