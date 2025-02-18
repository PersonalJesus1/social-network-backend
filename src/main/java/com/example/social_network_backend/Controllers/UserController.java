package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.User.CreateUserDTO;
import com.example.social_network_backend.DTO.User.UpdateUserDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    //@RequestBody связывает тело HTTP-запроса с параметром метода контроллера и преобразует его в объект Java
    // с помощью встроенного преобразователя (ObjectMapper в случае JSON).
    //При работе с POST, PUT, или PATCH запросами, где клиент отправляет данные (обычно JSON).
    // Примеры данных, которые можно передать: регистрация пользователя, обновление профиля, создание записи.
    @PostMapping
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody CreateUserDTO userDTO) {
        //Аннотация @RequestBody указывает, что данные запроса (в формате JSON или другого поддерживаемого формата)
        // должны быть автоматически преобразованы в объект CreateUserDTO. Это делает Spring через ObjectMapper.
        return ResponseEntity.ok(userService.createUser(userDTO));
        //Возвращаемый тип метода — это обертка ResponseEntity, которая содержит объект UserResponseDTO.
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable Long id) {
        //@PathVariable извлекает значение из части URL и связывает его с параметром метода контроллера.
        //Для работы с GET, PUT, DELETE запросами, где идентификатор ресурса передается в URL.
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}