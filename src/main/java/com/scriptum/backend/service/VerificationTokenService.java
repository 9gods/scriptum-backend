package com.scriptum.backend.service;

import com.scriptum.backend.infrastructure.database.repository.IVerificationTokenRepository;
import com.scriptum.backend.infrastructure.database.jpa.UserJpaEntity;
import com.scriptum.backend.infrastructure.database.jpa.VerificationToken;
import com.scriptum.backend.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final IVerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Value("${verification.token.expiry.minutes:1440}") // 24 hours by default
    private int tokenExpiryMinutes;

    @Transactional
    public void createVerificationToken(UserJpaEntity user) {
        // Delete any existing unverified tokens for this user
        Optional<VerificationToken> existingToken = tokenRepository.findByUserIdAndVerifiedFalse(user.getId());
        existingToken.ifPresent(token -> tokenRepository.delete(token));

        // Create a new token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(tokenExpiryMinutes))
                .verified(false)
                .build();

        tokenRepository.save(verificationToken);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    @Transactional
    public boolean verifyEmail(String token) {
        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);

        if (verificationToken.isEmpty() || verificationToken.get().isExpired()) {
            return false;
        }

        VerificationToken tokenEntity = verificationToken.get();
        UserJpaEntity user = tokenEntity.getUser();

        // Mark token as verified
        tokenEntity.setVerified(true);
        tokenRepository.save(tokenEntity);

        // Mark user's email as verified
        user.setEmailVerified(true);
        userService.save(user);

        return true;
    }

    @Transactional
    public void resendVerificationToken(UUID userId) {
        UserJpaEntity user = userService.findByIdOrElseThrow(userId);
        createVerificationToken(user);
    }
}