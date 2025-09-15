package com.opentranslation.management.repository;

import com.opentranslation.management.model.Locale;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocaleRepository extends JpaRepository<Locale, Long>
{
  Optional<Locale> findByCode(String code);
}
