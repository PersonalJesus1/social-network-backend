package com.example.social_network_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.social_network_backend.DTO.Message.CreateMessageDTO;
import com.example.social_network_backend.DTO.Message.UpdateMessageDTO;
import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Entities.User;
import com.example.social_network_backend.Repositories.MessageRepository;
import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.Services.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    private Message message;
    private User creator;
    private User receiver;
    private CreateMessageDTO createMessageDTO;
    private UpdateMessageDTO updateMessageDTO;

    @BeforeEach
    void setUp() {
        creator = new User();
        creator.setId(1L);
        receiver = new User();
        receiver.setId(2L);

        message = new Message();
        message.setId(1L);
        message.setCreator(creator);
        message.setReceiver(receiver);
        message.setText("Hello");

        createMessageDTO = new CreateMessageDTO("Hello", creator.getId(), receiver.getId());
        updateMessageDTO = new UpdateMessageDTO("Updated text");
    }

    @Test
    void createMessage_Success() {
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message createdMessage = messageService.createMessage(createMessageDTO, creator, receiver);

        assertNotNull(createdMessage);
        assertEquals(message.getText(), createdMessage.getText());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void getAllMessages_Success() {
        Page<Message> page = new PageImpl<>(List.of(message));
        when(messageRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Message> result = messageService.getAllMessages(Pageable.unpaged());

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateMessage_Success() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message updatedMessage = messageService.updateMessage(1L, updateMessageDTO);

        assertNotNull(updatedMessage);
        assertEquals(updateMessageDTO.text(), updatedMessage.getText());
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void updateMessage_ThrowsException_WhenMessageNotFound() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> messageService.updateMessage(1L, updateMessageDTO));
    }

    @Test
    void deleteMessage_Success() {
        when(messageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(messageRepository).deleteById(1L);

        assertDoesNotThrow(() -> messageService.deleteMessage(1L));
        verify(messageRepository).deleteById(1L);
    }

    @Test
    void deleteMessage_ThrowsException_WhenMessageNotFound() {
        when(messageRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(1L));
    }

    @Test
    void getMessagesByUserId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(messageRepository.findByCreatorId(1L)).thenReturn(List.of(message));

        List<Message> messages = messageService.getMessagesByUserId(1L);

        assertFalse(messages.isEmpty());
        assertEquals(1, messages.size());
    }

    @Test
    void getMessagesByUserId_ThrowsException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> messageService.getMessagesByUserId(1L));
    }
}