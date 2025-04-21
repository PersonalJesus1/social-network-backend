package com.example.social_network_backend.DTO.Post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostDTO(
       @NotBlank(message = "Text cannot be blank")
       @Size(max = 2000, message = "Text of the post cannot exceed 2000 characters")  String text,
        String pictureBase64
) { }
