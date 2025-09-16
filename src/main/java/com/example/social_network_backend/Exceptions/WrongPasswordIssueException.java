package com.example.social_network_backend.Exceptions;

public class WrongPasswordIssueException extends RuntimeException {
    public WrongPasswordIssueException(String message) {
        super(message);
    }
}