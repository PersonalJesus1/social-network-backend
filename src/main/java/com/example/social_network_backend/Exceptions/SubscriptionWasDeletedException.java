package com.example.social_network_backend.Exceptions;

public class SubscriptionWasDeletedException extends RuntimeException {
    public SubscriptionWasDeletedException(String message) {
        super(message);
    }
}

