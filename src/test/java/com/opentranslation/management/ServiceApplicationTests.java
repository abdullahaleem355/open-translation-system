package com.opentranslation.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.opentranslation.management.controller.AuthController;
import com.opentranslation.management.controller.LocaleController;
import com.opentranslation.management.controller.TagController;
import com.opentranslation.management.controller.TranslationController;
import com.opentranslation.management.dto.LocaleDto;
import com.opentranslation.management.dto.TagDto;
import com.opentranslation.management.dto.TranslationRequest;
import com.opentranslation.management.dto.TranslationResponse;
import com.opentranslation.management.security.JwtUtil;
import com.opentranslation.management.service.LocaleService;
import com.opentranslation.management.service.TagService;
import com.opentranslation.management.service.TranslationService;

@SpringBootTest
class ServiceApplicationTests
{

  @Nested
  class AuthControllerTests
  {
    private AuthController authController;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp()
    {
      jwtUtil = mock(JwtUtil.class);
      authController = new AuthController(jwtUtil);
    }

    @Test
    @DisplayName("Should return JWT token when valid client code is provided")
    void givenValidClientCode_whenGenerateToken_thenReturnToken()
    {
      String clientCode = "CLIENT_ABC";
      given(jwtUtil.generateToken(clientCode)).willReturn("jwt-token");

      ResponseEntity<?> response = authController.generateToken(clientCode);

      assertThat(response.getStatusCodeValue()).isEqualTo(200);
      assertThat(response.getBody()).isInstanceOf(Map.class);
      assertThat(((Map<?, ?>) response.getBody()).get("token")).isEqualTo("jwt-token");
    }

    @Test
    @DisplayName("Should return 401 Unauthorized when invalid client code is provided")
    void givenInvalidClientCode_whenGenerateToken_thenUnauthorized()
    {
      ResponseEntity<?> response = authController.generateToken("INVALID");

      assertThat(response.getStatusCodeValue()).isEqualTo(401);
      assertThat(response.getBody()).isEqualTo("Invalid client code");
    }
  }

  @Nested
  class LocaleControllerTests
  {
    private LocaleService localeService;
    private LocaleController localeController;

    @BeforeEach
    void setUp()
    {
      localeService = mock(LocaleService.class);
      localeController = new LocaleController(localeService);
    }

    @Test
    @DisplayName("Should create a locale and return LocaleDto")
    void givenCode_whenCreateLocale_thenReturnDto()
    {
      LocaleDto dto = new LocaleDto(1L, "en");
      given(localeService.createLocale("en")).willReturn(dto);

      ResponseEntity<LocaleDto> response = localeController.createLocale("en");

      assertThat(response.getBody()
                         .getCode()).isEqualTo("en");
    }

    @Test
    @DisplayName("Should return LocaleDto for a given ID")
    void givenId_whenGetLocale_thenReturnDto()
    {
      LocaleDto dto = new LocaleDto(1L, "en");
      given(localeService.getLocale(1L)).willReturn(dto);

      ResponseEntity<LocaleDto> response = localeController.getLocale(1L);

      assertThat(response.getBody()
                         .getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return list of all locales")
    void whenGetAllLocales_thenReturnList()
    {
      given(localeService.getAllLocales()).willReturn(List.of(new LocaleDto(1L, "en")));

      ResponseEntity<List<LocaleDto>> response = localeController.getAllLocales();

      assertThat(response.getBody()).hasSize(1);
    }
  }

  @Nested
  class TagControllerTests
  {
    private TagService tagService;
    private TagController tagController;

    @BeforeEach
    void setUp()
    {
      tagService = mock(TagService.class);
      tagController = new TagController(tagService);
    }

    @Test
    @DisplayName("Should create a tag and return TagDto")
    void givenName_whenCreateTag_thenReturnDto()
    {
      TagDto dto = new TagDto(1L, "ui");
      given(tagService.createTag("ui")).willReturn(dto);

      ResponseEntity<TagDto> response = tagController.createTag("ui");

      assertThat(response.getBody()
                         .getName()).isEqualTo("ui");
    }

    @Test
    @DisplayName("Should return TagDto for a given ID")
    void givenId_whenGetTag_thenReturnDto()
    {
      TagDto dto = new TagDto(1L, "ui");
      given(tagService.getTag(1L)).willReturn(dto);

      ResponseEntity<TagDto> response = tagController.getTag(1L);

      assertThat(response.getBody()
                         .getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return list of all tags")
    void whenGetAllTags_thenReturnList()
    {
      given(tagService.getAllTags()).willReturn(List.of(new TagDto(1L, "ui")));

      ResponseEntity<List<TagDto>> response = tagController.getAllTags();

      assertThat(response.getBody()).hasSize(1);
    }
  }

  @Nested
  class TranslationControllerTests
  {
    private TranslationService translationService;
    private TranslationController translationController;

    @BeforeEach
    void setUp()
    {
      translationService = mock(TranslationService.class);
      translationController = new TranslationController(translationService);
    }

    @Test
    @DisplayName("Should create a translation and return TranslationResponse")
    void givenRequest_whenCreateTranslation_thenReturnResponse()
    {
      TranslationRequest req = new TranslationRequest("key1", "content1", "en", Set.of());
      TranslationResponse resp = TranslationResponse.builder()
                                                    .id(1L)
                                                    .translationKey("key1")
                                                    .localeCode("en")
                                                    .content("content1")
                                                    .tags(Set.of())
                                                    .createdOn(null)
                                                    .updatedOn(null)
                                                    .build();
      given(translationService.createTranslation(req)).willReturn(resp);

      ResponseEntity<TranslationResponse> response = translationController.createTranslation(req);

      assertThat(response.getBody()
                         .getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should update a translation and return TranslationResponse")
    void givenIdAndRequest_whenUpdateTranslation_thenReturnResponse()
    {
      TranslationRequest req = new TranslationRequest("key1", "content1", "en", Set.of());
      TranslationResponse resp = TranslationResponse.builder()
                                                    .id(1L)
                                                    .translationKey("key1")
                                                    .localeCode("en")
                                                    .content("content1")
                                                    .tags(Set.of())
                                                    .createdOn(null)
                                                    .updatedOn(null)
                                                    .build();
      given(translationService.updateTranslation(1L, req)).willReturn(resp);

      ResponseEntity<TranslationResponse> response = translationController.updateTranslation(1L, req);

      assertThat(response.getBody()
                         .getTranslationKey()).isEqualTo("key1");
    }

    @Test
    @DisplayName("Should return translation for a given ID")
    void givenId_whenGetTranslation_thenReturnResponse()
    {
      TranslationResponse resp = TranslationResponse.builder()
                                                    .id(1L)
                                                    .translationKey("key1")
                                                    .localeCode("en")
                                                    .content("content1")
                                                    .tags(Set.of())
                                                    .createdOn(null)
                                                    .updatedOn(null)
                                                    .build();
      given(translationService.getTranslation(1L)).willReturn(resp);

      ResponseEntity<TranslationResponse> response = translationController.getTranslation(1L);

      assertThat(response.getBody()
                         .getContent()).isEqualTo("content1");
    }
  }
}
