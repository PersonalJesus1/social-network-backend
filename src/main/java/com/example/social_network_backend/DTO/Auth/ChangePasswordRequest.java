package com.example.social_network_backend.DTO.Auth;

public record ChangePasswordRequest(String currentPassword, String newPassword, String confirmationPassword) {
}