package com.scriptum.backend.api.controllers;

import com.scriptum.backend.domain.request.TagRequestBody;
import com.scriptum.backend.domain.response.TagResponseBody;
import com.scriptum.backend.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponseBody>> getAllTags(@RequestParam UUID userId) {
        List<TagResponseBody> response = tagService.getAllTagResponsesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponseBody> getTagById(@PathVariable UUID id) {
        TagResponseBody response = tagService.getTagResponseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TagResponseBody>> searchTagsByName(
            @RequestParam UUID userId,
            @RequestParam String name) {
        List<TagResponseBody> response = tagService.searchTagResponsesByName(userId, name);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TagResponseBody> createTag(@Valid @RequestBody TagRequestBody requestBody) {
        TagResponseBody response = tagService.createTagFromRequest(requestBody);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseBody> updateTag(
            @PathVariable UUID id,
            @Valid @RequestBody TagRequestBody requestBody) {
        TagResponseBody response = tagService.updateTagFromRequest(id, requestBody);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}