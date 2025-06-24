package com.scriptum.backend.service;

import com.scriptum.backend.domain.request.AuthRequestBody;
import com.scriptum.backend.domain.request.UserRequestBody;
import com.scriptum.backend.domain.response.AuthResponseBody;
import com.scriptum.backend.infrastructure.database.jpa.UserJpaEntity;
import com.scriptum.backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenService verificationTokenService;

    @Transactional
    public AuthResponseBody register(UserRequestBody request) {
        // Check if user already exists
        Optional<UserJpaEntity> existingUser = userService.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Create new user
        UserJpaEntity user = new UserJpaEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatarUrl(request.getAvatarUrl());
        user.setEmailVerified(false);
        user.setNewUser(true);

        UserJpaEntity savedUser = userService.save(user);

        // Create and send verification token
        verificationTokenService.createVerificationToken(savedUser);

        String token = jwtService.generateToken(savedUser.getEmail());

        // Create response
        return AuthResponseBody.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .token(token)
                .emailVerified(savedUser.isEmailVerified())
                .newUser(savedUser.isNewUser())
                .build();
    }

    public AuthResponseBody login(AuthRequestBody request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Get user details
            UserJpaEntity user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Generate token
            String token = jwtService.generateToken(user.getEmail());

            // Create response
            return AuthResponseBody.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .token(token)
                    .emailVerified(user.isEmailVerified())
                    .newUser(user.isNewUser())
                    .build();
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public boolean verifyEmail(String token) {
        return verificationTokenService.verifyEmail(token);
    }

    public void resendVerificationToken(UUID userId) {
        verificationTokenService.resendVerificationToken(userId);
    }
}