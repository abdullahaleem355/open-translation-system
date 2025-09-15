package com.opentranslation.management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocaleDto
{
  private Long id;
  private String code;
}