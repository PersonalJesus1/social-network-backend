package com.example.social_network_backend.DTO.User;

import com.example.social_network_backend.Entities.UserRole;
import com.example.social_network_backend.Entities.UserSex;

public record ResponseUserDTO(Long userId, String userName, String userSurname, String userEmail, UserSex sex,
                              UserRole userRole) {
}