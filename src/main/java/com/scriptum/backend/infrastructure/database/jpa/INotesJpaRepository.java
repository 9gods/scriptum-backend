package com.scriptum.backend.infrastructure.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface INotesJpaRepository extends JpaRepository<Notes, UUID> {
    
    List<Notes> findByUserIdOrderByModifiedAtDesc(UUID userId);
    
    List<Notes> findByUserIdAndTitleContainingIgnoreCaseOrderByModifiedAtDesc(UUID userId, String title);
    
    List<Notes> findByUserIdAndContentContainingIgnoreCaseOrderByModifiedAtDesc(UUID userId, String content);
    
    List<Notes> findByUserIdAndTagsIdOrderByModifiedAtDesc(UUID userId, UUID tagId);
}