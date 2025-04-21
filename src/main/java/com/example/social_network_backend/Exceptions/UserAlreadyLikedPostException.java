package com.example.social_network_backend.Exceptions;

public class UserAlreadyLikedPostException extends RuntimeException {
    public UserAlreadyLikedPostException(String message) {
        super(message);
    }
}