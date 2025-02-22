package com.example.social_network_backend.Facades;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Services.MessageService;
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

    public ResponseMessageDTO createMessage(CreateMessageDTO messageDTO) {
        validate(messageDTO);
        return messageService.createMessage(messageDTO);
    }

    public List<ResponseMessageDTO> getMessagesByUserId(Long id) {
        return messageService.getMessagesByUserId(id);
    }

    public List<ResponseMessageDTO> getAllMessages() {
        return messageService.getAllMessages();
    }

    public ResponseMessageDTO updateMessage(Long id, UpdateMessageDTO dto) {
        validate(dto);
        return messageService.updateMessage(id, dto);
    }

    public void deleteMessage(Long id) {
        messageService.deleteMessage(id);
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
}