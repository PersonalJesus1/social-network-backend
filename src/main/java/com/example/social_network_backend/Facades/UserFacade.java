package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;

import java.util.Set;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Service
public class UserFacade {

    private final UserService userService;
    private final Validator validator;

    public ResponseUserDTO createUser(CreateUserDTO userDTO) {
        validate(userDTO);
        User user = userService.createUser(userDTO);
        return mapToResponseDto(user);
    }

    public ResponseUserDTO getUserById(Long id) {
        User user = userService.getUserById(id);
        return mapToResponseDto(user);
    }

    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ResponseUserDTO updateUser(Long id, UpdateUserDTO dto) {
        User user = userService.getUserById(id);
        validate(dto);
        userService.updateUser(id, dto);
        return mapToResponseDto(user);
    }

    public void deleteUser(Long id) {
        userService.getUserById(id);
        userService.deleteUser(id);
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private ResponseUserDTO mapToResponseDto(User user) {
        return new ResponseUserDTO(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getSex(), user.getRole());
    }
}