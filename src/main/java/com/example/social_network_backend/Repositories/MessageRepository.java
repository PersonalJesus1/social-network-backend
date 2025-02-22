package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}