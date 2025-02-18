package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Entities.UserSex;
import com.example.social_network_backend.JwtUtil;
import com.example.social_network_backend.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {


        private final UserRepository userRepository;
        private final JwtUtil jwtUtil;
        private final PasswordEncoder passwordEncoder;

        public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.jwtUtil = jwtUtil;
            this.passwordEncoder = passwordEncoder;
        }




    public ResponseUserDTO createUser(CreateUserDTO dto) {
      /*  User user = new User();
        user.setUserName(dto.getUserName());
        user.setUserSurname(dto.getUserSurname());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPassword(dto.getUserPassword());
        user.setSex(UserSex.valueOf(String.valueOf(dto.getSex())));
        user = userRepository.save(user);
        return mapToResponseDTO(user);*/
        ResponseUserDTO response = new ResponseUserDTO();
        response.setUserId(1L);
        response.setUserName("Test");
        response.setUserSurname("User");
        response.setUserEmail("testuser@example.com");
        return response;
    }

    public ResponseUserDTO getUserById(Long id) {
       /* User user = userRepository.findById(id).orElseThrow();
        return mapToResponseDTO(user);*/
        ResponseUserDTO response = new ResponseUserDTO();
        response.setUserId(id);
        response.setUserName("Test");
        response.setUserSurname("User");
        response.setUserEmail("testuser@example.com");
        return response;
    }

    public List<ResponseUserDTO> getAllUsers() {
        /*return userRepository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());*/
        ResponseUserDTO response = new ResponseUserDTO();
        response.setUserId(1L);
        response.setUserName("Test");
        response.setUserSurname("User");
        response.setUserEmail("testuser@example.com");
        return List.of(response);
    }

    public ResponseUserDTO updateUser(Long id, UpdateUserDTO dto) {
        /*User user = userRepository.findById(id).orElseThrow();
        user.setUserName(dto.getUserName());
        user.setUserSurname(dto.getUserSurname());
        user.setUserEmail(dto.getUserEmail());
        user = userRepository.save(user);
        return mapToResponseDTO(user);*/
        ResponseUserDTO response = new ResponseUserDTO();
        response.setUserId(id);
        response.setUserName(dto.getUserName());
        response.setUserSurname(dto.getUserSurname());
        response.setUserEmail(dto.getUserEmail());
        return response;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private ResponseUserDTO mapToResponseDTO(User user) {
        ResponseUserDTO response = new ResponseUserDTO();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setUserSurname(user.getUserSurname());
        response.setUserEmail(user.getUserEmail());
        return response;
    }

    public String authenticate(String email, String password) {
        //Optional<User> userOptional = userRepository.findByUserEmail(email);
        String testEmail = "john.doe@example.com";
        String testPassword = passwordEncoder.encode("password123"); // Захешированный пароль

        if (email.equals(testEmail) && passwordEncoder.matches(password, testPassword)) {
            return "dummy-jwt-token"; // Временный токен
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}