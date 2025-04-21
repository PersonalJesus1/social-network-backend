package com.example.social_network_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Entities.UserRole;
import com.example.social_network_backend.Entities.UserSex;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("hashedPassword");

        createUserDTO = new CreateUserDTO("John", "Doe", "john.doe@example.com", "password123", UserSex.MALE, UserRole.USER);
        updateUserDTO = new UpdateUserDTO("John", "Smith", "john.smith@example.com");
    }

    @Test
    void createUser_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(createUserDTO);

        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_Success() {
        Page<User> usersPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);

        Page<User> result = userService.getAllUsers(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, updateUserDTO);

        assertNotNull(updatedUser);
        assertEquals(updateUserDTO.name(), updatedUser.getName());
        assertEquals(updateUserDTO.surname(), updatedUser.getSurname());
    }

    @Test
    void updateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, updateUserDTO));
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userRepository).deleteById(1L);
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        String token = userService.authenticate(user.getEmail(), "password123");

        assertNotNull(token);
        assertEquals("dummy-jwt-token", token);
    }

    @Test
    void authenticate_InvalidPassword() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.authenticate(user.getEmail(), "wrongpassword"));
    }

    @Test
    void authenticate_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.authenticate("nonexistent@example.com", "password123"));
    }
}