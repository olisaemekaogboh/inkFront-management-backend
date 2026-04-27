package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.entity.HomepageSection;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.HomepageSectionMapper;
import com.inkFront.inFront.repository.HomepageSectionRepository;
import com.inkFront.inFront.service.content.HomepageSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return homepageSectionRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(homepageSectionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HomepageSectionDTO getById(Long id) {
        HomepageSection entity = homepageSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homepage section not found with id: " + id));

        return homepageSectionMapper.toDto(entity);
    }

    @Override
    public HomepageSectionDTO create(HomepageSectionDTO dto) {
        HomepageSection entity = homepageSectionMapper.toEntity(dto);
        applyDefaults(entity);

        HomepageSection saved = homepageSectionRepository.save(entity);
        return homepageSectionMapper.toDto(saved);
    }

    @Override
    public HomepageSectionDTO update(Long id, HomepageSectionDTO dto) {
        HomepageSection entity = homepageSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homepage section not found with id: " + id));

        homepageSectionMapper.updateEntityFromDto(dto, entity);
        applyDefaults(entity);

        HomepageSection saved = homepageSectionRepository.save(entity);
        return homepageSectionMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!homepageSectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Homepage section not found with id: " + id);
        }

        homepageSectionRepository.deleteById(id);
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
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        return homepageSectionRepository
                .findBySectionKeyIgnoreCaseAndLanguageAndStatus(sectionKey, safeLanguage, ContentStatus.PUBLISHED)
                .or(() -> {
                    if (safeLanguage == SupportedLanguage.EN) {
                        return java.util.Optional.empty();
                    }

                    return homepageSectionRepository.findBySectionKeyIgnoreCaseAndLanguageAndStatus(
                            sectionKey,
                            SupportedLanguage.EN,
                            ContentStatus.PUBLISHED
                    );
                })
                .map(homepageSectionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published homepage section not found for key: " + sectionKey
                ));
    }

    private List<HomepageSection> findPublishedSections(SupportedLanguage language) {
        return homepageSectionRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED
        );
    }

    private void applyDefaults(HomepageSection entity) {
        if (entity.getLanguage() == null) {
            entity.setLanguage(SupportedLanguage.EN);
        }

        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
        }
    }
}