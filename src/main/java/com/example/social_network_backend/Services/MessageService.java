package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.ResponseMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.DTO.User.ResponseUserDTO;
import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.MessageRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public ResponseMessageDTO createMessage(CreateMessageDTO dto) {
       /* User creator = userRepository.findById(dto.getCreatorId()).orElseThrow(());
        User receiver = userRepository.findById(dto.getReceiverId()).orElseThrow());
        Message message = new Message();
        message.setMessageText(dto.getMessageText());
        message.setMessageCreator(creator);
        message.setMessageReceiver(receiver);
        message = messageRepository.save(message);
        return mapToResponseDTO(message);*/
        ResponseMessageDTO response = new ResponseMessageDTO();
        response.setMessageText("Hello World");
        response.setCreatorName("Test1");
        response.setReceiverName("Test2");
        return response;

    }

    public List<ResponseMessageDTO> getAllMessages() {
      /*  return messageRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());*/
        ResponseMessageDTO response = new ResponseMessageDTO();
        response.setMessageText("Hello World");
        response.setCreatorName("Test1");
        response.setReceiverName("Test2");
       return List.of(response);
    }

    public ResponseMessageDTO updateMessage(Long id, UpdateMessageDTO dto) {
        /*Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        message.setMessageText(dto.getMessageText());
        message = messageRepository.save(message);
        return mapToResponseDTO(message);*/
        ResponseMessageDTO response = new ResponseMessageDTO();
        response.setMessageText("Hello World");
        response.setCreatorName("Test1");
        response.setReceiverName("Test2");
        return response;
    }

    public void deleteMessage(Long id) {
       // messageRepository.deleteById(id);
    }

    public List<ResponseMessageDTO> getMessagesByUserId(Long userId) {
        /*User user = userRepository.findById(userId).orElseThrow();
        List<Message> messages = messageRepository.findByMessageCreatorOrMessageReceiver(user, user);
        return messages.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());*/
        ResponseMessageDTO response = new ResponseMessageDTO();
        response.setMessageText("Hello World");
        response.setCreatorName("Test1");
        response.setReceiverName("Test2");
        return List.of(response);
    }

    private ResponseMessageDTO mapToResponseDTO(Message message) {
        ResponseMessageDTO response = new ResponseMessageDTO();
        response.setMessageText(message.getMessageText());
        response.setMessageDate(message.getMessageDate());
        response.setCreatorName(message.getMessageCreator() != null ? message.getMessageCreator().getUserName() : "Unknown");
        response.setReceiverName(message.getMessageReceiver() != null ? message.getMessageReceiver().getUserName() : "Unknown");
        return response;
    }
}