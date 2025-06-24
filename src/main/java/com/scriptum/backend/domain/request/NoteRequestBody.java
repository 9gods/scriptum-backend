package com.scriptum.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestBody {

    @NotBlank(message = "Title is required")
    private String title;
    
    private String content;
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    private Set<UUID> tagIds = new HashSet<>();
}