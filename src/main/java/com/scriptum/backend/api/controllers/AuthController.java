package com.scriptum.backend.api.controllers;

import com.scriptum.backend.domain.request.AuthRequestBody;
import com.scriptum.backend.domain.request.UserRequestBody;
import com.scriptum.backend.domain.response.AuthResponseBody;
import com.scriptum.backend.infrastructure.database.jpa.UserJpaEntity;
import com.scriptum.backend.infrastructure.security.JwtService;
import com.scriptum.backend.service.UserService;
import com.scriptum.backend.service.VerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenService verificationTokenService;

    @GetMapping("/health")
    public String healthCheck() {
        return "API running...";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseBody> register(@Valid @RequestBody UserRequestBody request) {
        // Check if user already exists
        Optional<UserJpaEntity> existingUser = userService.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
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
        AuthResponseBody response = AuthResponseBody.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .token(token)
                .emailVerified(savedUser.isEmailVerified())
                .newUser(savedUser.isNewUser())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseBody> login(@Valid @RequestBody AuthRequestBody request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Get user details
            UserJpaEntity user = userService.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Generate token
            String token = jwtService.generateToken(user.getEmail());

            // Create response
            AuthResponseBody response = AuthResponseBody.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .token(token)
                    .emailVerified(user.isEmailVerified())
                    .newUser(user.isNewUser())
                    .build();

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        boolean verified = verificationTokenService.verifyEmail(token);

        Map<String, Object> response = new HashMap<>();
        if (verified) {
            response.put("success", true);
            response.put("message", "Email verified successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, Object>> resendVerification(@RequestParam UUID userId) {
        try {
            verificationTokenService.resendVerificationToken(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verification email sent successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to send verification email: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
