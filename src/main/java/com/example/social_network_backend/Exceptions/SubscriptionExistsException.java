package com.example.social_network_backend.Exceptions;

public class SubscriptionExistsException extends RuntimeException {
    public SubscriptionExistsException(String message) {
        super(message);
    }
}