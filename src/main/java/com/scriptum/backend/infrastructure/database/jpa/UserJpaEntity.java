package com.scriptum.backend.infrastructure.database.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String password;
    private String avatarUrl;
    private boolean emailVerified;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
