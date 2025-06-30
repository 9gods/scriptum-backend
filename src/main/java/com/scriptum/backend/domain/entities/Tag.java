package com.scriptum.backend.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    private UUID id;
    
    private String name;
    
    private String color;
    
    private UUID userId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime modifiedAt;

}
