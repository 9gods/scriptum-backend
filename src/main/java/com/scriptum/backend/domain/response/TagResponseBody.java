package com.scriptum.backend.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseBody {

    private UUID id;
    
    private String name;
    
    private String color;
    
    private UUID userId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;
}