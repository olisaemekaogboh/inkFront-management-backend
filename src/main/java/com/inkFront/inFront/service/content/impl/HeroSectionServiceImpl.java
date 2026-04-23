package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.HeroSection;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.HeroSectionMapper;
import com.inkFront.inFront.repository.HeroSectionRepository;
import com.inkFront.inFront.service.content.HeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return heroSectionRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(heroSectionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HeroSectionDTO getById(Long id) {
        HeroSection entity = heroSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hero section not found with id: " + id));
        return heroSectionMapper.toDto(entity);
    }

    @Override
    public HeroSectionDTO create(HeroSectionDTO dto) {
        HeroSection entity = heroSectionMapper.toEntity(dto);

        if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }
        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }
        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
        }

        HeroSection saved = heroSectionRepository.save(entity);
        return heroSectionMapper.toDto(saved);
    }

    @Override
    public HeroSectionDTO update(Long id, HeroSectionDTO dto) {
        HeroSection entity = heroSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hero section not found with id: " + id));

        heroSectionMapper.updateEntityFromDto(dto, entity);

        HeroSection saved = heroSectionRepository.save(entity);
        return heroSectionMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!heroSectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hero section not found with id: " + id);
        }
        heroSectionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HeroSectionDTO> getPublishedHeroSections(
            SupportedLanguage language,
            String placement,
            boolean featuredOnly
    ) {
        List<HeroSection> items = featuredOnly
                ? heroSectionRepository.findByLanguageAndPlacementAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                language, placement, ContentStatus.PUBLISHED
        )
                : heroSectionRepository.findByLanguageAndPlacementAndStatusOrderByDisplayOrderAsc(
                language, placement, ContentStatus.PUBLISHED
        );

        return items.stream()
                .map(heroSectionMapper::toDto)
                .toList();
    }
}