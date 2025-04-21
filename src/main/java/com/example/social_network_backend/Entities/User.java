package com.example.social_network_backend.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "networkuser", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Enumerated(EnumType.STRING)
    private UserSex sex;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribed_to_id")
    )
    private List<User> subscriptions;
}