package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User createUser(CreateUserDTO dto) {
        return mapToEntity(dto);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<User> getAllUsers() {
        User user = new User();
        return List.of(user);
    }

    public User updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public String authenticate(String email, String password) {
        String testEmail = "john.doe@example.com";
        String testPassword = passwordEncoder.encode("password123");
        if (email.equals(testEmail) && passwordEncoder.matches(password, testPassword)) {
            return "dummy-jwt-token"; // Временный токен
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private User mapToEntity(CreateUserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setSex(dto.sex());
        return user;
    }
}