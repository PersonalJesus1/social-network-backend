package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Repositories.MessageRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ResponseMessageDTO createMessage(CreateMessageDTO dto) {
       /* User creator = userRepository.findById(dto.getCreatorId())
        .orElseThrow(() -> new EntityNotFoundException("Creator user not found"));
User receiver = userRepository.findById(dto.getReceiverId())
        .orElseThrow(() -> new EntityNotFoundException("Receiver user not found"));
        Message message = new Message();
        message.setMessageText(dto.getMessageText());
        message.setMessageCreator(creator);
        message.setMessageReceiver(receiver);
        message = messageRepository.save(message);
        return mapToResponseDTO(message);*/
        ResponseMessageDTO response = new ResponseMessageDTO("Hello World", LocalDateTime.now(), "Test1", "Test2");
        return response;
    }

    public List<ResponseMessageDTO> getAllMessages() {
      /*  return messageRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());*/
        ResponseMessageDTO response = new ResponseMessageDTO("Hello World", LocalDateTime.now(), "Test1", "Test2");
        return List.of(response);
    }

    public ResponseMessageDTO updateMessage(Long id, UpdateMessageDTO dto) {
        /*Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        message.setMessageText(dto.getMessageText());
        message = messageRepository.save(message);
        return mapToResponseDTO(message);*/
        ResponseMessageDTO response = new ResponseMessageDTO("Hello World", LocalDateTime.now(), "Test1", "Test2");
        return response;
    }

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        messageRepository.delete(message);
    }

    public List<ResponseMessageDTO> getMessagesByUserId(Long userId) {
        /*User user = userRepository.findById(userId).orElseThrow();
        List<Message> messages = messageRepository.findByMessageCreatorOrMessageReceiver(user, user);
        return messages.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());*/
        ResponseMessageDTO response = new ResponseMessageDTO("Hello World", LocalDateTime.now(), "Test1", "Test2");
        return List.of(response);
    }

    private ResponseMessageDTO mapToResponseDTO(Message message) {
        ResponseMessageDTO response = new ResponseMessageDTO(message.getText(), message.getDate(),  message.getCreator().getName(),
                message.getReceiver().getName());
        return response;
    }

    /*private Message mapToEntity(CreateMessageDTO dto) {
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new EntityNotFoundException("Creator user not found"));
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver user not found"));
        Message message = new Message();
        message.setMessageReceiver(userRepository.getReferenceById(dto.getReceiverId()));
        message.setMessageCreator(userRepository.getReferenceById(dto.getCreatorId()));
        message.setMessageText((dto.getMessageText()));
        return message;
    }*/
}