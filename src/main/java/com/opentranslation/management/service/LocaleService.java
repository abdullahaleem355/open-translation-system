package com.opentranslation.management.service;

import com.opentranslation.management.dto.LocaleDto;
import com.opentranslation.management.model.Locale;
import com.opentranslation.management.repository.LocaleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LocaleService
{

  private final LocaleRepository localeRepository;

  public LocaleDto createLocale(String code)
  {
    Locale entity = Locale.builder()
                          .code(code)
                          .build();
    return map(localeRepository.save(entity));
  }

  public LocaleDto getLocale(Long id)
  {
    return localeRepository.findById(id)
                           .map(this::map)
                           .orElseThrow(() -> new EntityNotFoundException("Locale not found: " + id));
  }

  public List<LocaleDto> getAllLocales()
  {
    return localeRepository.findAll()
                           .stream()
                           .map(this::map)
                           .toList();
  }

  private LocaleDto map(Locale entity)
  {
    return new LocaleDto(entity.getId(), entity.getCode());
  }
}
