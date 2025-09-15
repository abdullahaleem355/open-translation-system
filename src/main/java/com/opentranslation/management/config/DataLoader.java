package com.opentranslation.management.config;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.opentranslation.management.model.Locale;
import com.opentranslation.management.model.Tag;
import com.opentranslation.management.model.Translation;
import com.opentranslation.management.repository.LocaleRepository;
import com.opentranslation.management.repository.TagRepository;
import com.opentranslation.management.repository.TranslationRepository;

import lombok.RequiredArgsConstructor;

/**
 * Component responsible for populating the database with test data for scalability and performance testing.
 * <p>
 * This DataLoader creates a predefined set of locales and tags, and then generates a large number of translation entries (100,000 by default) with random
 * associations between locales and tags.
 * <p>
 * The insertion is batched for better performance. The loader will skip execution if translations already exist in the database.
 * <p>
 * Usage:
 * <ul>
 *     <li>Run the Spring Boot application; this loader executes automatically at startup.</li>
 *     <li>Useful for testing the application's performance, search capabilities, and export functionality.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner
{

  private final LocaleRepository localeRepository;
  private final TagRepository tagRepository;
  private final TranslationRepository translationRepository;

  private static final int TOTAL_RECORDS = 100_000;

  @Override
  @Transactional
  public void run(String... args) throws Exception
  {
    loadData();
  }

  @Transactional
  public void loadData()
  {

    if (translationRepository.count() > 0)
    {
      return; // skip if data already exists
    }

    // Ensure locales exist
    Locale en = localeRepository.findByCode("en")
                                .orElseGet(() -> localeRepository.save(Locale.builder()
                                                                             .code("en")
                                                                             .build()));
    Locale fr = localeRepository.findByCode("fr")
                                .orElseGet(() -> localeRepository.save(Locale.builder()
                                                                             .code("fr")
                                                                             .build()));
    Locale es = localeRepository.findByCode("es")
                                .orElseGet(() -> localeRepository.save(Locale.builder()
                                                                             .code("es")
                                                                             .build()));

    Locale[] locales = new Locale[]{en, fr, es};

    // Ensure some tags exist
    Tag general = tagRepository.findByName("general")
                               .orElseGet(() -> tagRepository.save(Tag.builder()
                                                                      .name("general")
                                                                      .build()));
    Tag ui = tagRepository.findByName("ui")
                          .orElseGet(() -> tagRepository.save(Tag.builder()
                                                                 .name("ui")
                                                                 .build()));
    Tag error = tagRepository.findByName("error")
                             .orElseGet(() -> tagRepository.save(Tag.builder()
                                                                    .name("error")
                                                                    .build()));

    Tag[] tags = new Tag[]{general, ui, error};

    Random random = new Random();
    Set<Translation> batch = new HashSet<>();

    for (int i = 1; i <= TOTAL_RECORDS; i++)
    {
      Locale locale = locales[random.nextInt(locales.length)];

      Set<Tag> translationTags = new HashSet<>();
      translationTags.add(tags[random.nextInt(tags.length)]);

      Translation t = Translation.builder()
                                 .translationKey("key_" + i)
                                 .locale(locale)
                                 .content("Sample content " + i)
                                 .tags(translationTags)
                                 .build();
      batch.add(t);
      if (i % 1000 == 0)
      {
        translationRepository.saveAll(batch);
        batch.clear();
        System.out.println("Inserted " + i + " translations");
      }
    }
    if (!batch.isEmpty())
    {
      translationRepository.saveAll(batch);
    }

    System.out.println("Test data insertion complete!");
  }

}
