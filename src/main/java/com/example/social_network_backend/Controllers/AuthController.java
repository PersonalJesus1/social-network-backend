package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.AuthRequest;
import com.example.social_network_backend.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

   /* @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String token = userService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        return ResponseEntity.ok(token);
    }*/
   @PostMapping("/login")
   public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {
       String token = userService.authenticate(authRequest.getEmail(), authRequest.getPassword());

       // Оборачиваем токен в JSON
       Map<String, String> response = new HashMap<>();
       response.put("token", token);

       return ResponseEntity.ok(response);
   }

}

