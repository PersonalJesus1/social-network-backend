package com.example.social_network_backend;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
public class DatabaseService {
    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void printDatabaseUrl() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("🔹 Подключение к БД: " + connection.getMetaData().getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

