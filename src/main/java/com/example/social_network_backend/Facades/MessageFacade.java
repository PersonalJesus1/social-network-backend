package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Services.MessageService;
import com.example.social_network_backend.Services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Service
public class MessageFacade {
    private final MessageService messageService;
    private final Validator validator;
    private final UserService userService;

    public ResponseMessageDTO createMessage(CreateMessageDTO dto) {
        User creator = userService.getUserById(dto.creatorId());
        User receiver = userService.getUserById(dto.receiverId());
        validate(dto);
        Message message = messageService.createMessage(dto, creator, receiver);
        return mapToResponseDto(message);
    }

    public List<ResponseMessageDTO> getAllMessages() {
        return messageService.getAllMessages()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ResponseMessageDTO updateMessage(Long id, UpdateMessageDTO dto) {
        userService.getUserById(id);
        validate(dto);
        Message message = messageService.updateMessage(id, dto);
        return mapToResponseDto(message);
    }

    public void deleteMessage(Long id) {
        messageService.deleteMessage(id);
    }

    public List<ResponseMessageDTO> getMessagesByUserId(Long id) {
        return messageService.getMessagesByUserId(id).stream().map(this::mapToResponseDto).toList();
    }

    private void validate(Object dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private ResponseMessageDTO mapToResponseDto(Message message) {
        return new ResponseMessageDTO(message.getText(), message.getDate(), message.getCreator().getName(), message.getReceiver().getName());
    }
}