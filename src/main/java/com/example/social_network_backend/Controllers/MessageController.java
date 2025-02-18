package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    @GetMapping
    public ResponseEntity<List<ResponseMessageDTO>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
    @PostMapping
    public ResponseEntity<ResponseMessageDTO> createMessage(@RequestBody CreateMessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.createMessage(messageDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> updateMessage(@PathVariable Long id, @RequestBody UpdateMessageDTO dto) {
        return ResponseEntity.ok(messageService.updateMessage(id, dto));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResponseMessageDTO>> getMessagesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getMessagesByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}