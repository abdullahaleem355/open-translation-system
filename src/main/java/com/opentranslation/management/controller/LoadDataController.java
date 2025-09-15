package com.opentranslation.management.controller;

import com.opentranslation.management.config.DataLoader;
import com.opentranslation.management.repository.TranslationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-data")
@RequiredArgsConstructor
public class LoadDataController {

  private final DataLoader dataLoader;
  private final TranslationRepository translationRepository;

  /**
   * Trigger insertion of test data on demand.
   * Useful for Dockerized environments without restarting the container.
   */
  @PostMapping("/load")
  public ResponseEntity<String> loadTestData(@RequestParam(defaultValue = "false") boolean force) {
    if (force) {
      translationRepository.deleteAll();
    }
    dataLoader.loadData();
    return ResponseEntity.ok("Test data insertion triggered!");
  }
}
