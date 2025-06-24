package com.scriptum.backend.infrastructure.database.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT", nullable = false)
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

}
