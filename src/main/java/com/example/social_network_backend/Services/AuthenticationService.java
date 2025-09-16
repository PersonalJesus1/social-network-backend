package com.example.social_network_backend.Services;

import com.example.social_network_backend.Config.JwtService;
import com.example.social_network_backend.DTO.Auth.AuthenticationResponse;
import com.example.social_network_backend.DTO.Auth.ChangePasswordRequest;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Exceptions.InvalidCredentialsException;
import com.example.social_network_backend.Exceptions.PasswordConformationIssueException;
import com.example.social_network_backend.Exceptions.UserExistsException;
import com.example.social_network_backend.Exceptions.WrongPasswordIssueException;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.token.Token;
import com.example.social_network_backend.token.TokenRepository;
import com.example.social_network_backend.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthenticationResponse register(User user) {
        if (repository.isExistedByEmail(user.getEmail())) {
            throw new UserExistsException();
        }

        User savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    @Transactional
    public AuthenticationResponse authenticate(User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        var foundUser = repository.findByEmail(user.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(foundUser);
        var refreshToken = jwtService.generateRefreshToken(foundUser);
        revokeAllUserTokens(foundUser);
        saveUserToken(foundUser, jwtToken);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken, refreshToken);
        return authenticationResponse;
    }

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Transactional
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        try {
            refreshToken = authHeader.substring(7);
            userEmail = jwtService.extractUsername(refreshToken);

            if (userEmail != null) {
                var user = this.repository.findByEmail(userEmail)
                        .orElseThrow();
                if (jwtService.isTokenValid(refreshToken, user)) {
                    var accessToken = jwtService.generateToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);
                    var authResponse = new AuthenticationResponse(accessToken, refreshToken);
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("Unauthorized");
        }
        User user = (User) authentication.getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new WrongPasswordIssueException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new PasswordConformationIssueException("Password and conformation are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // save the new password
        repository.save(user);
    }
}