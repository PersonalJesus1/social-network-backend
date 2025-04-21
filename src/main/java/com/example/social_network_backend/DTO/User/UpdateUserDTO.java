package com.example.social_network_backend.DTO.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @NotNull(message = "Name is required") @Size(max = 64, message = "Name cannot exceed 64 characters") String name,
        @NotNull(message = "Surname is required") @Size(max = 64, message = "Surname cannot exceed 64 characters") String surname,
        @NotNull(message = "Email is required") @Email(message = "Must have format of email") String email) {
}