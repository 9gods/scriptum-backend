package com.scriptum.backend.api.controllers;

import com.scriptum.backend.domain.request.NoteRequestBody;
import com.scriptum.backend.domain.response.NoteResponseBody;
import com.scriptum.backend.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteResponseBody>> getAllNotes(@RequestParam UUID userId) {
        List<NoteResponseBody> response = noteService.getAllNoteResponsesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseBody> getNoteById(@PathVariable UUID id) {
        NoteResponseBody response = noteService.getNoteResponseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<NoteResponseBody>> searchNotesByTitle(
            @RequestParam UUID userId,
            @RequestParam String title) {
        List<NoteResponseBody> response = noteService.searchNoteResponsesByTitle(userId, title);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/content")
    public ResponseEntity<List<NoteResponseBody>> searchNotesByContent(
            @RequestParam UUID userId,
            @RequestParam String content) {
        List<NoteResponseBody> response = noteService.searchNoteResponsesByContent(userId, content);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<NoteResponseBody>> getNotesByTag(
            @RequestParam UUID userId,
            @PathVariable UUID tagId) {
        List<NoteResponseBody> response = noteService.getNoteResponsesByTag(userId, tagId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<NoteResponseBody> createNote(@Valid @RequestBody NoteRequestBody requestBody) {
        NoteResponseBody response = noteService.createNoteFromRequest(requestBody);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseBody> updateNote(
            @PathVariable UUID id,
            @Valid @RequestBody NoteRequestBody requestBody) {
        NoteResponseBody response = noteService.updateNoteFromRequest(id, requestBody);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
