package com.opentranslation.management.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Locale
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 10)
  private String code;

  @OneToMany(mappedBy = "locale", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Translation> translations = new HashSet<>();
}
