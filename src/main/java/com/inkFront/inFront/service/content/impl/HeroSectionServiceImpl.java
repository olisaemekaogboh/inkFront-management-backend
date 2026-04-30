package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.HeroSection;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.InvalidRequestException;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.HeroSectionMapper;
import com.inkFront.inFront.repository.HeroSectionRepository;
import com.inkFront.inFront.service.content.HeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HeroSectionServiceImpl implements HeroSectionService {

    private final HeroSectionRepository heroSectionRepository;
    private final HeroSectionMapper heroSectionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HeroSectionDTO> getAll() {
        return heroSectionRepository
                .findAll(Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by("id")))
                .stream()
                .map(heroSectionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HeroSectionDTO getById(Long id) {
        return heroSectionMapper.toDto(findById(id));
    }

    @Override
    public HeroSectionDTO create(HeroSectionDTO dto) {
        HeroSection entity = new HeroSection();
        applyFields(dto, entity, true);
        return heroSectionMapper.toDto(heroSectionRepository.save(entity));
    }

    @Override
    public HeroSectionDTO update(Long id, HeroSectionDTO dto) {
        HeroSection entity = findById(id);
        applyFields(dto, entity, false);
        return heroSectionMapper.toDto(heroSectionRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        heroSectionRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HeroSectionDTO> getPublishedHeroSections(
            SupportedLanguage language,
            String placement,
            boolean featuredOnly
    ) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;
        String safePlacement = StringUtils.hasText(placement) ? placement.trim() : "HOME";

        List<HeroSection> items = findPublishedHeroSections(safeLanguage, safePlacement, featuredOnly);

        if (items.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            items = findPublishedHeroSections(SupportedLanguage.EN, safePlacement, featuredOnly);
        }

        return items.stream()
                .map(heroSectionMapper::toDto)
                .toList();
    }

    private HeroSection findById(Long id) {
        return heroSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Hero section not found with id: " + id
                ));
    }

    private List<HeroSection> findPublishedHeroSections(
            SupportedLanguage language,
            String placement,
            boolean featuredOnly
    ) {
        if (featuredOnly) {
            return heroSectionRepository.findByLanguageAndPlacementAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                    language,
                    placement,
                    ContentStatus.PUBLISHED
            );
        }

        return heroSectionRepository.findByLanguageAndPlacementAndStatusOrderByDisplayOrderAsc(
                language,
                placement,
                ContentStatus.PUBLISHED
        );
    }

    private void applyFields(HeroSectionDTO dto, HeroSection entity, boolean creating) {
        if (dto == null) {
            throw new InvalidRequestException("Hero section data is required");
        }

        entity.setTitle(required(
                first(dto.getTitle(), entity.getTitle()),
                "Hero title is required"
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

        entity.setBackgroundImageUrl(first(
                dto.getBackgroundImageUrl(),
                dto.getImageUrl(),
                entity.getBackgroundImageUrl()
        ));

        entity.setPlacement(required(
                first(dto.getPlacement(), entity.getPlacement(), "HOME"),
                "Hero placement is required"
        ));

        entity.setPrimaryButtonLabel(first(
                dto.getPrimaryButtonLabel(),
                entity.getPrimaryButtonLabel()
        ));

        entity.setPrimaryButtonUrl(first(
                dto.getPrimaryButtonUrl(),
                entity.getPrimaryButtonUrl()
        ));

        entity.setSecondaryButtonLabel(first(
                dto.getSecondaryButtonLabel(),
                entity.getSecondaryButtonLabel()
        ));

        entity.setSecondaryButtonUrl(first(
                dto.getSecondaryButtonUrl(),
                entity.getSecondaryButtonUrl()
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