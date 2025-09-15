package com.opentranslation.management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.opentranslation.management.dto.LocaleDto;
import com.opentranslation.management.service.LocaleService;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsible for managing locales in the translation system.
 * <p>
 * Provides endpoints to create, retrieve by ID, and list all locales.
 * </p>
 */
@RestController
@RequestMapping("/api/locales")
@RequiredArgsConstructor
public class LocaleController
{

  /**
   * Service responsible for locale-related operations.
   */
  private final LocaleService localeService;

  /**
   * Creates a new locale with the specified code.
   *
   * @param code the locale code (e.g., "en_US", "fr_FR")
   * @return {@link ResponseEntity} containing the created {@link LocaleDto}
   */
  @PostMapping
  public ResponseEntity<LocaleDto> createLocale(@RequestParam String code)
  {
    return ResponseEntity.ok(localeService.createLocale(code));
  }

  /**
   * Retrieves a locale by its unique ID.
   *
   * @param id the ID of the locale
   * @return {@link ResponseEntity} containing the {@link LocaleDto} for the given ID
   */
  @GetMapping("/{id}")
  public ResponseEntity<LocaleDto> getLocale(@PathVariable Long id)
  {
    return ResponseEntity.ok(localeService.getLocale(id));
  }

  /**
   * Retrieves all available locales.
   *
   * @return {@link ResponseEntity} containing a list of all {@link LocaleDto} objects
   */
  @GetMapping
  public ResponseEntity<List<LocaleDto>> getAllLocales()
  {
    return ResponseEntity.ok(localeService.getAllLocales());
  }
}
