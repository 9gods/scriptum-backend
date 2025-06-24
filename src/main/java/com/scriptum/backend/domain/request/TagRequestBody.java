package com.scriptum.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRequestBody {

    @NotBlank(message = "Name is required")
    private String name;
    
    private String color;
    
    @NotNull(message = "User ID is required")
    private UUID userId;
}