package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
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
        return userService.createUser(userDTO);
    }

    public ResponseUserDTO getUserById(Long id) {
        return userService.getUserById(id);
    }

    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    public ResponseUserDTO updateUser(Long id, UpdateUserDTO dto) {
        validate(dto);
        return userService.updateUser(id, dto);
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
}