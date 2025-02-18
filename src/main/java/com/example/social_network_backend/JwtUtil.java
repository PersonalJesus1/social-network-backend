package com.example.social_network_backend;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your-secret-key"; // Храни в .env или application.properties

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)  // Email пользователя в качестве идентификатора
                .setIssuedAt(new Date())  // Дата выпуска
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 часов
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        return extractClaims(token).getExpiration().after(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8)) // Передаем ключ в байтах
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

