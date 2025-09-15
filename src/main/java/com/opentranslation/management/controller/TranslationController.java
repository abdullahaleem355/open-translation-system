package com.opentranslation.management.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.opentranslation.management.dto.TranslationRequest;
import com.opentranslation.management.dto.TranslationResponse;
import com.opentranslation.management.service.TranslationService;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsible for managing translations in multiple locales.
 * <p>
 * Provides endpoints to create, update, retrieve, search, and export translations. Optimized for large datasets.
 * </p>
 */
@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
public class TranslationController
{

  private final TranslationService translationService;

  @PostMapping
  public ResponseEntity<TranslationResponse> createTranslation(@RequestBody TranslationRequest request)
  {
    return ResponseEntity.ok(translationService.createTranslation(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TranslationResponse> updateTranslation(@PathVariable Long id, @RequestBody TranslationRequest request)
  {
    return ResponseEntity.ok(translationService.updateTranslation(id, request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TranslationResponse> getTranslation(@PathVariable Long id)
  {
    return ResponseEntity.ok(translationService.getTranslation(id));
  }

  /**
   * Search translations with pagination. Only one filter type can be applied per request.
   */
  @GetMapping("/search")
  public ResponseEntity<Page<TranslationResponse>> searchTranslations(@RequestParam(required = false) String key,
                                                                      @RequestParam(required = false) String locale,
                                                                      @RequestParam(required = false) String content,
                                                                      @RequestParam(required = false) String tag,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "50") int size)
  {

    PageRequest pageRequest = PageRequest.of(page, size);

    if (key != null && locale != null)
    {
      return ResponseEntity.ok(translationService.searchByKeyAndLocale(key, locale, pageRequest));
    }
    else if (content != null)
    {
      return ResponseEntity.ok(translationService.searchByContent(content, pageRequest));
    }
    else if (tag != null)
    {
      return ResponseEntity.ok(translationService.searchByTag(tag, pageRequest));
    }

    return ResponseEntity.badRequest()
                         .build();
  }

  /**
   * Export translations as nested JSON grouped by locale and translation key. Uses streaming to efficiently handle large datasets.
   */
  @GetMapping("/export")
  public ResponseEntity<Map<String, Map<String, String>>> exportTranslations()
  {
    Map<String, Map<String, String>> export = translationService.exportTranslations();
    return ResponseEntity.ok(export);
  }
}
