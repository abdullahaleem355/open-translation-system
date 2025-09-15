package com.opentranslation.management.service;

import com.opentranslation.management.dto.TagDto;
import com.opentranslation.management.model.Tag;
import com.opentranslation.management.repository.TagRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService
{

  private final TagRepository tagRepository;

  public TagDto createTag(String name)
  {
    Tag entity = Tag.builder()
                    .name(name)
                    .build();
    return map(tagRepository.save(entity));
  }

  public TagDto getTag(Long id)
  {
    return tagRepository.findById(id)
                        .map(this::map)
                        .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + id));
  }

  public List<TagDto> getAllTags()
  {
    return tagRepository.findAll()
                        .stream()
                        .map(this::map)
                        .toList();
  }

  private TagDto map(Tag entity)
  {
    return new TagDto(entity.getId(), entity.getName());
  }
}
