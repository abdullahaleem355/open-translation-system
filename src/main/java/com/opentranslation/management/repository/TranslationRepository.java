package com.opentranslation.management.repository;

import java.util.stream.Stream;
import java.util.Set;
import java.util.stream.Collectors;

import com.opentranslation.management.dto.TranslationResponse;
import com.opentranslation.management.model.Translation;
import com.opentranslation.management.model.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TranslationRepository extends JpaRepository<Translation, Long> {

  /**
   * Find translations by key and locale.
   * Tags are not included in JPQL to avoid constructor issues; they can be mapped in service layer.
   */
  @Query("SELECT new com.opentranslation.management.dto.TranslationResponse(" +
         "t.id, t.translationKey, t.locale.code, t.content, null, t.createdOn, t.updatedOn) " +
         "FROM Translation t WHERE t.translationKey = :key AND t.locale.code = :locale")
  Page<TranslationResponse> findByKeyAndLocale(@Param("key") String key,
                                               @Param("locale") String locale,
                                               Pageable pageable);

  /**
   * Search translations by content containing a string (case-insensitive).
   * Tags are set to null and can be mapped later in the service layer.
   */
  @Query("SELECT new com.opentranslation.management.dto.TranslationResponse(" +
         "t.id, t.translationKey, t.locale.code, t.content, null, t.createdOn, t.updatedOn) " +
         "FROM Translation t WHERE LOWER(t.content) LIKE LOWER(CONCAT('%', :content, '%'))")
  Page<TranslationResponse> findByContentContaining(@Param("content") String content, Pageable pageable);

  /**
   * Find translations by tag name.
   * Here we fetch entities to map tags manually in service layer to avoid JPQL constructor issues.
   */
  @Query("SELECT t FROM Translation t JOIN t.tags tag WHERE tag.name = :tag")
  Page<Translation> findByTagName(@Param("tag") String tag, Pageable pageable);

  /**
   * Stream all translations.
   * Tags are set to null in DTO; mapping to Set<String> should be done in the service layer.
   */
  @Query("SELECT new com.opentranslation.management.dto.TranslationResponse(" +
         "t.id, t.translationKey, t.locale.code, t.content, null, t.createdOn, t.updatedOn) " +
         "FROM Translation t")
  Stream<TranslationResponse> streamAllTranslations();
}
