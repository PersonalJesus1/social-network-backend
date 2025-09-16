package com.example.social_network_backend;

import com.example.social_network_backend.Config.JwtService;
import com.example.social_network_backend.DTO.Auth.AuthenticationResponse;
import com.example.social_network_backend.DTO.Auth.ChangePasswordRequest;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Exceptions.InvalidCredentialsException;
import com.example.social_network_backend.Exceptions.PasswordConformationIssueException;
import com.example.social_network_backend.Exceptions.UserExistsException;
import com.example.social_network_backend.Exceptions.WrongPasswordIssueException;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.AuthenticationService;
import com.example.social_network_backend.token.Token;
import com.example.social_network_backend.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void register_Success() {
        when(userRepository.isExistedByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.register(user);

        assertNotNull(response);
        assertEquals("jwtToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void register_UserAlreadyExists() {
        when(userRepository.isExistedByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserExistsException.class, () -> authenticationService.register(user));
        verify(userRepository, never()).save(any());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(user);

        assertNotNull(response);
        assertEquals("jwtToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void authenticate_InvalidCredentials() {
        doThrow(new BadCredentialsException("Invalid")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.authenticate(user));
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void refreshToken_Success() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validRefreshToken");
        when(jwtService.extractUsername("validRefreshToken")).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("validRefreshToken", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new MockServletOutputStream(outputStream));

        authenticationService.refreshToken(request, response);

        String jsonResponse = outputStream.toString();
        assertTrue(jsonResponse.contains("newAccessToken"));
        assertTrue(jsonResponse.contains("validRefreshToken"));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void refreshToken_InvalidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenThrow(new RuntimeException("Invalid"));

        assertThrows(InvalidCredentialsException.class,
                () -> authenticationService.refreshToken(request, response));
    }

    @Test
    void changePassword_Success() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);

        ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass", "newPass");

        when(passwordEncoder.matches("oldPass", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        authenticationService.changePassword(request, auth);

        verify(userRepository).save(user);
        assertEquals("encodedNewPass", user.getPassword());
    }

    @Test
    void changePassword_WrongCurrentPassword() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);

        ChangePasswordRequest request = new ChangePasswordRequest("wrongPass", "newPass", "newPass");

        when(passwordEncoder.matches("wrongPass", user.getPassword())).thenReturn(false);

        assertThrows(WrongPasswordIssueException.class, () ->
                authenticationService.changePassword(request, auth));
    }

    @Test
    void changePassword_PasswordMismatch() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);

        ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass", "differentPass");

        when(passwordEncoder.matches("oldPass", user.getPassword())).thenReturn(true);

        assertThrows(PasswordConformationIssueException.class, () ->
                authenticationService.changePassword(request, auth));
    }

    @Test
    void changePassword_Unauthorized() {
        Authentication auth = null;

        ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass", "newPass");

        assertThrows(RuntimeException.class, () ->
                authenticationService.changePassword(request, auth));
    }

    private static class MockServletOutputStream extends jakarta.servlet.ServletOutputStream {
        private final ByteArrayOutputStream outputStream;

        public MockServletOutputStream(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) {
            outputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
        }
    }
}
