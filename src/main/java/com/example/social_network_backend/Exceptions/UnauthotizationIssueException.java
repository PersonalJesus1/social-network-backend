package com.example.social_network_backend.Exceptions;

public class UnauthotizationIssueException extends RuntimeException {
    public UnauthotizationIssueException(String message) {
        super(message);
    }
}