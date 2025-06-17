package com.scriptum.backend.api.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/health")
    public String healthCheck() {
        return "API running...";
    }
}
