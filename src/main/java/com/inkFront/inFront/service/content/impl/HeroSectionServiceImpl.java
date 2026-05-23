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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
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
        log.info("Creating new hero section with title: {}", dto.getTitle());
        HeroSection entity = new HeroSection();
        applyFields(dto, entity, true);

        // Ensure published items are featured for homepage
        if (entity.getStatus() == ContentStatus.PUBLISHED && "HOME".equals(entity.getPlacement())) {
            entity.setFeatured(true);
            log.info("Auto-set featured=true for published HOME hero section");
        }

        HeroSection saved = heroSectionRepository.save(entity);
        log.info("Created hero section with id: {}, status: {}", saved.getId(), saved.getStatus());
        return heroSectionMapper.toDto(saved);
    }

    @Override
    public HeroSectionDTO update(Long id, HeroSectionDTO dto) {
        log.info("Updating hero section with id: {}", id);
        log.info("Input DTO - status: {}, active: {}, title: {}", dto.getStatus(), dto.getActive(), dto.getTitle());

        HeroSection entity = findById(id);
        log.info("Existing entity - status: {}, featured: {}", entity.getStatus(), entity.getFeatured());

        applyFields(dto, entity, false);

        // Ensure published items are featured for homepage
        if (entity.getStatus() == ContentStatus.PUBLISHED && "HOME".equals(entity.getPlacement())) {
            entity.setFeatured(true);
            log.info("Auto-set featured=true for published HOME hero section");
        }

        HeroSection saved = heroSectionRepository.save(entity);
        log.info("Saved hero section - id: {}, status: {}, featured: {}", saved.getId(), saved.getStatus(), saved.getFeatured());

        return heroSectionMapper.toDto(saved);
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

        log.info("Fetching published hero sections - language: {}, placement: {}, featuredOnly: {}",
                safeLanguage, safePlacement, featuredOnly);

        List<HeroSectionDTO> items;

        if (featuredOnly) {
            items = heroSectionRepository.findFeaturedPublishedHeroSectionsDTO(
                    safeLanguage, safePlacement, ContentStatus.PUBLISHED
            );
        } else {
            items = heroSectionRepository.findPublishedHeroSectionsDTO(
                    safeLanguage, safePlacement, ContentStatus.PUBLISHED
            );
        }

        log.info("Found {} items for language: {}", items.size(), safeLanguage);

        if (items.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            log.info("No items found for {}, falling back to EN", safeLanguage);
            if (featuredOnly) {
                items = heroSectionRepository.findFeaturedPublishedHeroSectionsDTO(
                        SupportedLanguage.EN, safePlacement, ContentStatus.PUBLISHED
                );
            } else {
                items = heroSectionRepository.findPublishedHeroSectionsDTO(
                        SupportedLanguage.EN, safePlacement, ContentStatus.PUBLISHED
                );
            }
            log.info("Found {} items in fallback EN", items.size());
        }

        return items;
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

        // CRITICAL FIX: Status handling
        if (dto.getStatus() != null) {
            log.info("Setting status from dto.getStatus(): {}", dto.getStatus());
            entity.setStatus(dto.getStatus());
        } else if (dto.getActive() != null) {
            ContentStatus newStatus = Boolean.TRUE.equals(dto.getActive())
                    ? ContentStatus.PUBLISHED
                    : ContentStatus.DRAFT;
            log.info("Setting status from dto.getActive(): {} -> {}", dto.getActive(), newStatus);
            entity.setStatus(newStatus);
        } else if (creating) {
            // Only set default for new entities
            log.info("Setting default DRAFT status for new entity");
            entity.setStatus(ContentStatus.DRAFT);
        } else {
            // On update, if no status is provided, log and preserve existing status
            log.info("No status provided in update, preserving existing status: {}", entity.getStatus());
        }
        // IMPORTANT: On update, if no status is provided, KEEP the existing status
        // Do nothing - preserve current status
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

    @Override
    @Transactional(readOnly = true)
    public List<HeroSection> getAllEntities() {
        return heroSectionRepository.findAll();
    }

    private String required(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new InvalidRequestException(message);
        }

        return value.trim();
    }
}