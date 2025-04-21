package com.example.social_network_backend.Services;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.MessageRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Message createMessage(CreateMessageDTO dto, User creator, User receiver) {
        Message message = mapToEntity(dto, creator, receiver);
        messageRepository.save(message);
        return message;
    }

    public Page<Message> getAllMessages(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }

    @Transactional
    public Message updateMessage(Long id, UpdateMessageDTO dto) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        message.setText(dto.text());
        return messageRepository.save(message);
    }

    @Transactional
    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new EntityNotFoundException("Message with ID " + id + " not found");
        }
        messageRepository.deleteById(id);
    }

    public List<Message> getMessagesByUserId(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));

        return messageRepository.findByCreatorId(id);
    }

    private Message mapToEntity(CreateMessageDTO dto, User creator, User receiver) {
        Message message = new Message();
        message.setCreator(creator);
        message.setReceiver(receiver);
        message.setText(dto.text());
        return message;
    }
}