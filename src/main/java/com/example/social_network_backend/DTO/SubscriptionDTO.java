package com.example.social_network_backend.DTO;

import jakarta.validation.constraints.NotNull;

public record SubscriptionDTO(
        @NotNull(message = "Can not be null") Long followerId,
        @NotNull(message = "Can not be null") Long followingId) {
}