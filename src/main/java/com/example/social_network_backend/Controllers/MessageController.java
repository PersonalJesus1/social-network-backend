package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Facades.MessageFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageFacade messageFacade;

    @GetMapping
    public ResponseEntity<List<ResponseMessageDTO>> getAllMessages() {
        return ResponseEntity.ok(messageFacade.getAllMessages());
    }

    @PostMapping
    public ResponseEntity<ResponseMessageDTO> createMessage(@Valid @RequestBody CreateMessageDTO messageDTO) {
        return ResponseEntity.ok(messageFacade.createMessage(messageDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> updateMessage( @PathVariable Long id, @Valid @RequestBody UpdateMessageDTO dto) {
        return ResponseEntity.ok(messageFacade.updateMessage(id, dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResponseMessageDTO>> getMessagesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(messageFacade.getMessagesByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageFacade.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}