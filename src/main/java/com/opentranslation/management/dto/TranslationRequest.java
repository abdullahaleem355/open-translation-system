package com.opentranslation.management.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationRequest
{
  private String translationKey;
  private String localeCode;
  private String content;
  private Set<String> tags;
}
