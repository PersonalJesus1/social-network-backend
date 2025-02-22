package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}