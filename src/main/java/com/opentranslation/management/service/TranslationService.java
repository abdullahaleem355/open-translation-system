package com.opentranslation.management.service;

import com.opentranslation.management.dto.TranslationRequest;
import com.opentranslation.management.dto.TranslationResponse;
import com.opentranslation.management.model.Locale;
import com.opentranslation.management.model.Tag;
import com.opentranslation.management.model.Translation;
import com.opentranslation.management.repository.LocaleRepository;
import com.opentranslation.management.repository.TagRepository;
import com.opentranslation.management.repository.TranslationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class TranslationService
{

  private final TranslationRepository translationRepository;
  private final LocaleRepository localeRepository;
  private final TagRepository tagRepository;

  public TranslationResponse createTranslation(TranslationRequest request)
  {
    Locale locale = localeRepository.findByCode(request.getLocaleCode())
                                    .orElseThrow(() -> new EntityNotFoundException("Locale not found: " + request.getLocaleCode()));

    Set<Tag> tags = resolveTags(request.getTags());

    Translation entity = Translation.builder()
                                    .translationKey(request.getTranslationKey())
                                    .locale(locale)
                                    .content(request.getContent())
                                    .tags(tags)
                                    .build();

    Translation saved = translationRepository.save(entity);
    return mapToResponse(saved);
  }

  public TranslationResponse updateTranslation(Long id, TranslationRequest request)
  {
    Translation entity = translationRepository.findById(id)
                                              .orElseThrow(() -> new EntityNotFoundException("Translation not found: " + id));

    Locale locale = localeRepository.findByCode(request.getLocaleCode())
                                    .orElseThrow(() -> new EntityNotFoundException("Locale not found: " + request.getLocaleCode()));

    entity.setTranslationKey(request.getTranslationKey());
    entity.setLocale(locale);
    entity.setContent(request.getContent());
    entity.setTags(resolveTags(request.getTags()));
    entity.setUpdatedOn(java.time.OffsetDateTime.now());

    Translation updated = translationRepository.save(entity);
    return mapToResponse(updated);
  }

  public TranslationResponse getTranslation(Long id)
  {
    return translationRepository.findById(id)
                                .map(this::mapToResponse)
                                .orElseThrow(() -> new EntityNotFoundException("Translation not found: " + id));
  }

  // ---------------------- Search ----------------------

  public Page<TranslationResponse> searchByKeyAndLocale(String key, String localeCode, Pageable pageable)
  {
    return translationRepository.findByKeyAndLocale(key, localeCode, pageable)
                                .map(this::enrichTags);
  }

  public Page<TranslationResponse> searchByContent(String content, Pageable pageable)
  {
    return translationRepository.findByContentContaining(content, pageable)
                                .map(this::enrichTags);
  }

  public Page<TranslationResponse> searchByTag(String tag, Pageable pageable)
  {
    return translationRepository.findByTagName(tag, pageable)
                                .map(this::mapToResponse); // convert entities to DTO
  }

  @Transactional(readOnly = true)
  public Map<String, Map<String, String>> exportTranslations()
  {
    try (Stream<TranslationResponse> stream = translationRepository.streamAllTranslations())
    {
      return stream.map(this::enrichTags) // populate tags if needed
                   .collect(Collectors.groupingBy(TranslationResponse::getLocaleCode,
                                                  Collectors.toMap(TranslationResponse::getTranslationKey,
                                                                   TranslationResponse::getContent,
                                                                   (existing, replacement) -> replacement,
                                                                   LinkedHashMap::new)));
    }
  }

  private Set<Tag> resolveTags(Set<String> tagNames)
  {
    if (tagNames == null || tagNames.isEmpty())
    {
      return new HashSet<>();
    }

    return tagNames.stream()
                   .map(name -> tagRepository.findByName(name)
                                             .orElseGet(() -> tagRepository.save(Tag.builder()
                                                                                    .name(name)
                                                                                    .build())))
                   .collect(Collectors.toSet());
  }

  private TranslationResponse mapToResponse(Translation entity)
  {
    return new TranslationResponse(entity.getId(),
                                   entity.getTranslationKey(),
                                   entity.getLocale()
                                         .getCode(),
                                   entity.getContent(),
                                   entity.getTags()
                                         .stream()
                                         .map(Tag::getName)
                                         .collect(Collectors.toSet()),
                                   entity.getCreatedOn(),
                                   entity.getUpdatedOn());
  }

  /**
   * Enrich a DTO with tags if it was created via JPQL without tags. Fetch tags from the entity by key+locale.
   */
  private TranslationResponse enrichTags(TranslationResponse dto)
  {
    Translation entity = translationRepository.findById(dto.getId())
                                              .orElseThrow(() -> new EntityNotFoundException("Translation not found: " + dto.getId()));
    dto.setTags(entity.getTags()
                      .stream()
                      .map(Tag::getName)
                      .collect(Collectors.toSet()));
    return dto;
  }
}
