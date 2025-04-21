package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(CreateUserDTO dto) {
        User user = mapToEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash pass
        userRepository.save(user);
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public User updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        userRepository.deleteById(id);
    }

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return "dummy-jwt-token"; // JWT
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private User mapToEntity(CreateUserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password())); // Хешируем пароль
        user.setSex(dto.sex());
        user.setRole(dto.role());
        return user;
    }
}