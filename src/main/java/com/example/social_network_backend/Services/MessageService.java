package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.MessageRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Message createMessage(CreateMessageDTO dto, User creator, User receiver) {
        Message message = mapToEntity(dto, creator, receiver);
        messageRepository.save(message);
        return message;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll().stream().toList();
    }

    public Message updateMessage(Long id, UpdateMessageDTO dto) {
    Message message = messageRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));

    message.setText(dto.text());
    return messageRepository.save(message);
}

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        messageRepository.delete(message);
    }

    public List<Message> getMessagesByUserId(Long id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        return messageRepository.findById(id).stream().toList();
    }

    private Message mapToEntity(CreateMessageDTO dto, User creator, User receiver) {
        Message message = new Message();
        message.setCreator(creator);
        message.setReceiver(receiver);
        message.setText(dto.text());
        return message;
    }
}