package com.example.social_network_backend.DTO.Post;

import com.example.social_network_backend.Entities.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePostDTO(
        @NotBlank(message = "Text cannot be blank") @Size(max = 2000, message = "Text of the post cannot exceed 2000 characters") String text,
        Image image) {
}