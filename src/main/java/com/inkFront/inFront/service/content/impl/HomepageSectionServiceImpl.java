package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.entity.HomepageSection;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.DuplicateResourceException;
import com.inkFront.inFront.exception.InvalidRequestException;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.HomepageSectionMapper;
import com.inkFront.inFront.repository.HomepageSectionRepository;
import com.inkFront.inFront.service.content.HomepageSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HomepageSectionServiceImpl implements HomepageSectionService {

    private final HomepageSectionRepository homepageSectionRepository;
    private final HomepageSectionMapper homepageSectionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HomepageSectionDTO> getAll() {
        return homepageSectionRepository
                .findAll(Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by("id")))
                .stream()
                .map(homepageSectionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HomepageSectionDTO getById(Long id) {
        return homepageSectionMapper.toDto(findById(id));
    }

    @Override
    public HomepageSectionDTO create(HomepageSectionDTO dto) {
        HomepageSection entity = new HomepageSection();
        applyFields(dto, entity, true);
        validateSectionKeyLanguageUnique(entity.getSectionKey(), entity.getLanguage(), null);
        return homepageSectionMapper.toDto(homepageSectionRepository.save(entity));
    }

    @Override
    public HomepageSectionDTO update(Long id, HomepageSectionDTO dto) {
        HomepageSection entity = findById(id);
        applyFields(dto, entity, false);
        validateSectionKeyLanguageUnique(entity.getSectionKey(), entity.getLanguage(), id);
        return homepageSectionMapper.toDto(homepageSectionRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        homepageSectionRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomepageSectionDTO> getPublishedSections(SupportedLanguage language) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        List<HomepageSection> sections = findPublishedSections(safeLanguage);

        if (sections.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            sections = findPublishedSections(SupportedLanguage.EN);
        }

        return sections.stream()
                .map(homepageSectionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HomepageSectionDTO getPublishedSectionByKey(String sectionKey, SupportedLanguage language) {
        String safeSectionKey = required(sectionKey, "Section key is required");
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        return homepageSectionRepository
                .findBySectionKeyIgnoreCaseAndLanguageAndStatus(
                        safeSectionKey,
                        safeLanguage,
                        ContentStatus.PUBLISHED
                )
                .or(() -> safeLanguage == SupportedLanguage.EN
                        ? java.util.Optional.empty()
                        : homepageSectionRepository.findBySectionKeyIgnoreCaseAndLanguageAndStatus(
                        safeSectionKey,
                        SupportedLanguage.EN,
                        ContentStatus.PUBLISHED
                ))
                .map(homepageSectionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published homepage section not found for key: " + safeSectionKey
                ));
    }

    private HomepageSection findById(Long id) {
        return homepageSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Homepage section not found with id: " + id
                ));
    }

    private List<HomepageSection> findPublishedSections(SupportedLanguage language) {
        return homepageSectionRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED
        );
    }

    private void applyFields(HomepageSectionDTO dto, HomepageSection entity, boolean creating) {
        if (dto == null) {
            throw new InvalidRequestException("Homepage section data is required");
        }

        entity.setSectionKey(required(
                first(dto.getSectionKey(), entity.getSectionKey()),
                "Section key is required"
        ));

        entity.setTitle(required(
                first(dto.getTitle(), entity.getTitle()),
                "Homepage section title is required"
        ));

        entity.setSubtitle(first(
                dto.getSubtitle(),
                entity.getSubtitle()
        ));

        entity.setBody(first(
                dto.getBody(),
                dto.getDescription(),
                entity.getBody()
        ));

        entity.setImageUrl(first(
                dto.getImageUrl(),
                entity.getImageUrl()
        ));

        if (dto.getLanguage() != null) {
            entity.setLanguage(dto.getLanguage());
        } else if (entity.getLanguage() == null) {
            entity.setLanguage(SupportedLanguage.EN);
        }

        if (dto.getDisplayOrder() != null) {
            entity.setDisplayOrder(dto.getDisplayOrder());
        } else if (dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        } else if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (dto.getFeatured() != null) {
            entity.setFeatured(dto.getFeatured());
        } else if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }

        if (dto.getActive() != null) {
            entity.setStatus(Boolean.TRUE.equals(dto.getActive())
                    ? ContentStatus.PUBLISHED
                    : ContentStatus.DRAFT);
        } else if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        } else if (entity.getStatus() == null) {
            entity.setStatus(creating ? ContentStatus.DRAFT : ContentStatus.DRAFT);
        }
    }

    private void validateSectionKeyLanguageUnique(
            String sectionKey,
            SupportedLanguage language,
            Long currentId
    ) {
        boolean exists = currentId == null
                ? homepageSectionRepository.existsBySectionKeyIgnoreCaseAndLanguage(sectionKey, language)
                : homepageSectionRepository.existsBySectionKeyIgnoreCaseAndLanguageAndIdNot(
                sectionKey,
                language,
                currentId
        );

        if (exists) {
            throw new DuplicateResourceException(
                    "Homepage section key already exists for language " + language + ": " + sectionKey
            );
        }
    }

    private String first(String... values) {
        if (values == null) return null;

        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }

        return null;
    }

    private String required(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new InvalidRequestException(message);
        }

        return value.trim();
    }
}