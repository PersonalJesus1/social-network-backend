package com.example.social_network_backend.Services;

import com.example.social_network_backend.Entities.Message;
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
    public Message createMessage(Message message) {
        messageRepository.save(message);
        return message;
    }

    public Page<Message> getAllMessages(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }

    @Transactional
    public Message updateMessage(Long id, Message message) {
        Message existedMessage = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        existedMessage.setText(message.getText());
        return messageRepository.save(existedMessage);
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
}