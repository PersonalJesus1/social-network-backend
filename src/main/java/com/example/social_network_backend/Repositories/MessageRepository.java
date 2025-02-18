package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.Message;
import com.example.social_network_backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByMessageCreatorOrMessageReceiver(User creator, User receiver);

}
