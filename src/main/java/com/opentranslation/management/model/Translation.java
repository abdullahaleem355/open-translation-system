package com.opentranslation.management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "translations", uniqueConstraints = @UniqueConstraint(columnNames = {"translation_key", "locale_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Translation
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "translation_key", nullable = false, length = 255)
  private String translationKey;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "locale_id", nullable = false)
  private Locale locale;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "created_on", nullable = false, updatable = false)
  private OffsetDateTime createdOn;

  @Column(name = "updated_on", nullable = false)
  private OffsetDateTime updatedOn;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "translation_tags", joinColumns = @JoinColumn(name = "translation_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
  @Builder.Default
  private Set<Tag> tags = new HashSet<>();

  @PrePersist
  public void prePersist()
  {
    createdOn = OffsetDateTime.now();
    updatedOn = createdOn;
  }

  @PreUpdate
  public void preUpdate()
  {
    updatedOn = OffsetDateTime.now();
  }
}
