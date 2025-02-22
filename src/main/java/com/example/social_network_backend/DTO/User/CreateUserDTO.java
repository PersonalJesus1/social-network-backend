package com.example.social_network_backend.DTO.User;

import com.example.social_network_backend.Entities.UserRole;
import com.example.social_network_backend.Entities.UserSex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(@NotNull(message = "Name is required")@Size(max = 64, message = "Name cannot exceed 64 characters")  String name,
                            @NotNull(message = "Surname is required")@Size(max = 64, message = "Surname cannot exceed 64 characters") String surname,
                            @NotNull(message = "Email is required") @Email(message = "Must have format of email")  String email,
                            @NotNull(message = "Password is required") @Size(min = 8, max = 14, message = "Password should be from 8 to 14 characters")  String password,
                            @NotNull(message = "Sex is required") UserSex sex,
                            @NotNull(message = "This field is required") UserRole userRole) {
}