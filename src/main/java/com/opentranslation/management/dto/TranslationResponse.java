package com.opentranslation.management.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationResponse
{
  private Long id;
  private String translationKey;
  private String localeCode;
  private String content;
  private Set<String> tags;
  private OffsetDateTime createdOn;
  private OffsetDateTime updatedOn;
}
