package com.example.social_network_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.social_network_backend")
public class SocialNetworkBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkBackendApplication.class, args);
    }
}