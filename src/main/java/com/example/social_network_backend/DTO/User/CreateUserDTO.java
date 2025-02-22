package com.example.social_network_backend.DTO.User;

import com.example.social_network_backend.Entities.UserSex;

public record CreateUserDTO(String userName, String userSurname, String userEmail, String userPassword, UserSex sex) {
}