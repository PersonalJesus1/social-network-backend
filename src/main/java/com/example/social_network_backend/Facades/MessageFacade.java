package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseUpdatedMessageDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Message message = messageService.createMessage(mapToEntity(dto, creator, receiver));
        return mapToResponseDto(message);
    }

    public List<ResponseMessageDTO> getAllMessages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageService.getAllMessages(pageable)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ResponseUpdatedMessageDTO updateMessage(Long id, UpdateMessageDTO dto, Authentication authentication) {
        validate(dto);
        Message updatedMessage = new Message();
        updatedMessage.setUpdatedDate(LocalDateTime.now());
        updatedMessage.setText(dto.text());
        Message message = messageService.updateMessage(id, updatedMessage, authentication);
        return mapToUpdatedResponseDto(message);
    }

    public ResponseMessageDTO getMessage(Long messageId, Authentication authentication) {
        return mapToResponseDto(messageService.getMessage(messageId, authentication));
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
        return new ResponseMessageDTO(message.getId(), message.getText(), message.getCreatedDate(), message.getCreator().getName(), message.getReceiver().getName());
    }

    private ResponseUpdatedMessageDTO mapToUpdatedResponseDto(Message message) {
        return new ResponseUpdatedMessageDTO(message.getId(), message.getText(), message.getUpdatedDate(), message.getCreator().getName(), message.getReceiver().getName());
    }

    private Message mapToEntity(CreateMessageDTO dto, User creator, User receiver) {
        Message message = new Message();
        message.setCreator(creator);
        message.setReceiver(receiver);
        message.setText(dto.text());
        return message;
    }
}