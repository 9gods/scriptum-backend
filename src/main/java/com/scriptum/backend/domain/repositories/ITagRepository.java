package com.scriptum.backend.domain.repositories;

import com.scriptum.backend.domain.entities.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITagRepository {

    List<Tag> findAllByUserId(UUID userId);

    Optional<Tag> findById(UUID id);

    Optional<Tag> findByUserIdAndName(UUID userId, String name);

    List<Tag> findByUserIdAndNameContaining(UUID userId, String name);

    Tag save(Tag tag);

    void deleteById(UUID id);
}
