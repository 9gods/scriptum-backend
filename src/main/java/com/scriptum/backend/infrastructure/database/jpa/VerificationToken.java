package com.scriptum.backend.infrastructure.database.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "VERIFICATION_TOKEN")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserJpaEntity user;

    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "VERIFIED")
    private boolean verified;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}