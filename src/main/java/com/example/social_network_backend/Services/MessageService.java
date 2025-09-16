package com.example.social_network_backend.Services;

import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Exceptions.AccessDeniedException;
import com.example.social_network_backend.Repositories.MessageRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
    public Message updateMessage(Long id, Message message, Authentication authentication) {
        Message existedMessage = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID " + id + " not found"));
        String currentUserEmail = authentication.getName();
        boolean isCreator = existedMessage.getCreator().getEmail().equals(currentUserEmail);
        boolean isReceiver = existedMessage.getReceiver().getEmail().equals(currentUserEmail);
        if (!(isCreator || isReceiver)) {
            throw new AccessDeniedException("You are not allowed to access this message");
        }
        existedMessage.setText(message.getText());
        return messageRepository.save(existedMessage);
    }

    public Message getMessage(Long messageId, Authentication authentication) {
        Message existedMessage = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Message with ID " + messageId + " not found"));

        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new EntityNotFoundException());
        boolean isCreator = existedMessage.getCreator().getEmail().equals(currentUserEmail);
        boolean isReceiver = existedMessage.getReceiver().getEmail().equals(currentUserEmail);
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        if (!(isCreator || isReceiver || isAdmin)) {
            throw new AccessDeniedException("You are not allowed to access this message");
        }
        return existedMessage;
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