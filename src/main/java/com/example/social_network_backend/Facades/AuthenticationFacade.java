package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Auth.AuthenticationRequestDTO;
import com.example.social_network_backend.DTO.Auth.ChangePasswordRequest;
import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.DTO.Auth.AuthenticationResponse;
import com.example.social_network_backend.Services.AuthenticationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthenticationFacade {
    private final Validator validator;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(CreateUserDTO createUserDTO) {
        validate(createUserDTO);
        User user = mapToEntity(createUserDTO);
        return authenticationService.register(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        validate(authenticationRequestDTO);
        User user = new User();
        user.setEmail(authenticationRequestDTO.email());
        user.setPassword(authenticationRequestDTO.password());
        return authenticationService.authenticate(user);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        authenticationService.refreshToken(request, response);
    }

    public void changePassword(ChangePasswordRequest request, Authentication authentication) {
        authenticationService.changePassword(request, authentication);
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public User mapToEntity(CreateUserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setSex(dto.sex());
        user.setRole(dto.role());
        return user;
    }
}