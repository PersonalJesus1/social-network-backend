package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Service
public class UserFacade {

    private final UserService userService;
    private final Validator validator;

    public ResponseUserDTO getUserById(Long id) {
        User user = userService.getUserById(id);
        return mapToResponseDto(user);
    }

    public List<ResponseUserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public String getAdminStats(Authentication authentication) {
        return userService.getAdminStats(authentication);
    }

    public ResponseUserDTO updateUser(Long id, UpdateUserDTO dto) {
        validate(dto);
        User user = new User();
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        User updatedUser = userService.updateUser(id, user);
        return mapToResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public ResponseUserDTO mapToResponseDto(User user) {
        return new ResponseUserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getSex(), user.getRole());
    }
}