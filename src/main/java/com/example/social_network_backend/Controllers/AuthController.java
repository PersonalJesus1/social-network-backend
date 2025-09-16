package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Auth.AuthenticationRequestDTO;
import com.example.social_network_backend.DTO.Auth.ChangePasswordRequest;
import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.Facades.AuthenticationFacade;
import com.example.social_network_backend.DTO.Auth.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationFacade authenticationFacade;

    public AuthController(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody CreateUserDTO CreateUserDTO) {
        return ResponseEntity.ok(authenticationFacade.register(CreateUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        AuthenticationResponse response = authenticationFacade.authenticate(authenticationRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        authenticationFacade.refreshToken(request, response);
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        authenticationFacade.changePassword(request, authentication);
        return ResponseEntity.ok().build();
    }
}