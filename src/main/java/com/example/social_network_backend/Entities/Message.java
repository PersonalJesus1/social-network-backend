package com.example.social_network_backend.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    @NotNull(message = "Message text cannot be null")
    private String messageText;

    private LocalDateTime messageDate;

    @ManyToOne
    @JoinColumn(name = "message_creator_user_id")
    private User messageCreator;

    @ManyToOne
    @JoinColumn(name = "message_receiver_user_id")
    private User messageReceiver;

    @PrePersist
    public void setDateBeforeInsert() {
        if (this.messageDate == null) {
            this.messageDate = LocalDateTime.now();
        }
    }


    // Getters and Setters
    public Long getMessageId() {
        return messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public User getMessageCreator() {
        return messageCreator;
    }

    public void setMessageCreator(User messageCreator) {
        this.messageCreator = messageCreator;
    }

    public User getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(User messageReceiver) {
        this.messageReceiver = messageReceiver;
    }
}

