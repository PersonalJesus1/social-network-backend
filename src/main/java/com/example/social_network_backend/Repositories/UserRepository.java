package com.example.social_network_backend.Repositories;

import com.example.social_network_backend.Entities.User;



import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByUserEmail(String email);

    /*
    Методы, которые уже предоставляются:
save(Entity entity) — сохраняет или обновляет сущность.
findById(Long id) — находит сущность по ID.
findAll() — возвращает список всех сущностей.
deleteById(Long id) — удаляет сущность по ID.
     */
}
