package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.Facades.UserFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserFacade userFacade;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userFacade.getUserById(id));
    }
    @GetMapping("/stats")
    public ResponseEntity<String> getStats(Authentication authentication) {
        return ResponseEntity.ok(userFacade.getAdminStats(authentication));
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ResponseEntity.ok(userFacade.getAllUsers(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userFacade.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userFacade.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}