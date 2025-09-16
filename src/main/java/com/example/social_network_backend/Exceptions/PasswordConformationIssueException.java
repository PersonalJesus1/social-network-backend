package com.example.social_network_backend.Exceptions;

public class PasswordConformationIssueException extends RuntimeException {
    public PasswordConformationIssueException(String message) {
        super(message);
    }
}