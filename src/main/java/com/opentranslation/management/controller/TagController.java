package com.opentranslation.management.controller;

import com.opentranslation.management.dto.TagDto;
import com.opentranslation.management.service.TagService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing tags in the translation system.
 * <p>
 * Provides endpoints to create, retrieve by ID, and list all tags.
 * </p>
 */
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController
{

  /**
   * Service responsible for tag-related operations.
   */
  private final TagService tagService;

  /**
   * Creates a new tag with the specified name.
   *
   * @param name the name of the tag (e.g., "login", "error", "ui")
   * @return {@link ResponseEntity} containing the created {@link TagDto}
   */
  @PostMapping
  public ResponseEntity<TagDto> createTag(@RequestParam String name)
  {
    return ResponseEntity.ok(tagService.createTag(name));
  }

  /**
   * Retrieves a tag by its unique ID.
   *
   * @param id the ID of the tag
   * @return {@link ResponseEntity} containing the {@link TagDto} for the given ID
   */
  @GetMapping("/{id}")
  public ResponseEntity<TagDto> getTag(@PathVariable Long id)
  {
    return ResponseEntity.ok(tagService.getTag(id));
  }

  /**
   * Retrieves all available tags.
   *
   * @return {@link ResponseEntity} containing a list of all {@link TagDto} objects
   */
  @GetMapping
  public ResponseEntity<List<TagDto>> getAllTags()
  {
    return ResponseEntity.ok(tagService.getAllTags());
  }
}
