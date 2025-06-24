package com.scriptum.backend.domain.repositories;

import com.scriptum.backend.domain.entities.Note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface INoteRepository {

    List<Note> findAllByUserId(UUID userId);

    Optional<Note> findById(UUID id);

    List<Note> findByUserIdAndTitleContaining(UUID userId, String title);

    List<Note> findByUserIdAndContentContaining(UUID userId, String content);

    List<Note> findByUserIdAndTagId(UUID userId, UUID tagId);

    Note save(Note note);

    void deleteById(UUID id);
}
