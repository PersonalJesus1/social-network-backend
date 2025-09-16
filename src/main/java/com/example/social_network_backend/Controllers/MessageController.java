package com.example.social_network_backend.Controllers;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseUpdatedMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Facades.MessageFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageFacade messageFacade;

    @GetMapping
    public ResponseEntity<List<ResponseMessageDTO>> getAllMessages(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                   @RequestParam(defaultValue = "10") @Min(1) int size) {
        return ResponseEntity.ok(messageFacade.getAllMessages(page, size));
    }

    @PostMapping
    public ResponseEntity<ResponseMessageDTO> createMessage(@Valid @RequestBody CreateMessageDTO messageDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageFacade.createMessage(messageDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUpdatedMessageDTO> updateMessage(@PathVariable Long id, @Valid @RequestBody UpdateMessageDTO dto,Authentication authentication) {
        return ResponseEntity.ok(messageFacade.updateMessage(id, dto,authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> getMessage(@PathVariable Long id,Authentication authentication) {
        return ResponseEntity.ok(messageFacade.getMessage(id, authentication));
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