package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.Facades.UserFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    public ResponseEntity<ResponseUserDTO> createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        return ResponseEntity.ok(userFacade.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userFacade.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return ResponseEntity.ok(userFacade.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser( @PathVariable Long id,@Valid @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userFacade.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userFacade.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}