package com.scriptum.backend.infrastructure.database.repository;

import com.scriptum.backend.infrastructure.database.jpa.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITagJpaRepository extends JpaRepository<Tag, UUID> {
    
    List<Tag> findByUserIdOrderByNameAsc(UUID userId);
    
    Optional<Tag> findByUserIdAndName(UUID userId, String name);
    
    List<Tag> findByUserIdAndNameContainingIgnoreCaseOrderByNameAsc(UUID userId, String name);

}
