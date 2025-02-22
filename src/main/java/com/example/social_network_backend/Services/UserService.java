package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.JwtUtil;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.social_network_backend.Entities.UserRole.ADMIN_ROLE;
import static com.example.social_network_backend.Entities.UserSex.MALE;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    public ResponseUserDTO createUser(CreateUserDTO dto) {
      /*  User user = new User();
        user.setUserName(dto.getUserName());
        user.setUserSurname(dto.getUserSurname());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPassword(dto.getUserPassword());
        user.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));
        user.setSex(UserSex.valueOf(String.valueOf(dto.getSex())));
        user = userRepository.save(user);
        return mapToResponseDTO(user);*/
        ResponseUserDTO response = new ResponseUserDTO(1L, "Test", "User", "testuser@example.com", MALE, ADMIN_ROLE);
        return response;
    }

    public ResponseUserDTO getUserById(Long id) {
       /*  User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
        return mapToResponseDTO(user);*/
        return new ResponseUserDTO(id, "Test", "User", "testuser@example.com", MALE, ADMIN_ROLE);
    }

    public List<ResponseUserDTO> getAllUsers() {
        /*return userRepository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());*/
        ResponseUserDTO response = new ResponseUserDTO(1L, "Test", "User", "testuser@example.com", MALE, ADMIN_ROLE);

        return List.of(response);
    }

    public ResponseUserDTO updateUser(Long id, UpdateUserDTO dto) {
        /*User user = userRepository.findById(id).orElseThrow();
        user.setUserName(dto.getUserName());
        user.setUserSurname(dto.getUserSurname());
        user.setUserEmail(dto.getUserEmail());
        user = userRepository.save(user);
        return mapToResponseDTO(user);*/
        return new ResponseUserDTO(id, dto.userName(), dto.userSurname(), dto.userEmail(), MALE, ADMIN_ROLE);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    private ResponseUserDTO mapToResponseDTO(User user) {
        return new ResponseUserDTO(1L, "Test", "User", "testuser@example.com", MALE, ADMIN_ROLE);
    }

    public String authenticate(String email, String password) {
        //Optional<User> userOptional = userRepository.findByUserEmail(email);
        String testEmail = "john.doe@example.com";
        String testPassword = passwordEncoder.encode("password123");
        if (email.equals(testEmail) && passwordEncoder.matches(password, testPassword)) {
            return "dummy-jwt-token"; // Временный токен
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

   /* private User mapToEntity(CreateUserDTO dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setUserSurname(dto.getUserSurname());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPassword(dto.getUserPassword());
        user.setSex(dto.getSex());
        return user;
    }*/
}