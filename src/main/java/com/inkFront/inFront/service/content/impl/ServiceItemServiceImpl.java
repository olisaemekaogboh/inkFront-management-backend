package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.api.PageResponse;
import com.inkFront.inFront.dto.content.ServiceDTO;
import com.inkFront.inFront.entity.ServiceItem;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.ServiceItemMapper;
import com.inkFront.inFront.repository.ServiceItemRepository;
import com.inkFront.inFront.service.content.ServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceItemServiceImpl implements ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final ServiceItemMapper serviceItemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDTO> getAll() {
        return serviceItemRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(serviceItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDTO getById(Long id) {
        ServiceItem entity = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service item not found with id: " + id));

        return serviceItemMapper.toDto(entity);
    }

    @Override
    public ServiceDTO create(ServiceDTO dto) {
        ServiceItem entity = serviceItemMapper.toEntity(dto);
        applyCompatibleFields(dto, entity, true);

        if (serviceItemRepository.existsBySlug(entity.getSlug())) {
            throw new IllegalArgumentException("Service slug already exists: " + entity.getSlug());
        }

        ServiceItem saved = serviceItemRepository.save(entity);
        return serviceItemMapper.toDto(saved);
    }

    @Override
    public ServiceDTO update(Long id, ServiceDTO dto) {
        ServiceItem entity = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service item not found with id: " + id));

        serviceItemMapper.updateEntityFromDto(dto, entity);
        applyCompatibleFields(dto, entity, false);

        if (serviceItemRepository.existsBySlugAndIdNot(entity.getSlug(), id)) {
            throw new IllegalArgumentException("Service slug already exists: " + entity.getSlug());
        }

        ServiceItem saved = serviceItemRepository.save(entity);
        return serviceItemMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!serviceItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service item not found with id: " + id);
        }

        serviceItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ServiceDTO> getPublishedServices(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        Page<ServiceItem> page = findPublishedServicesPage(safeLanguage, featuredOnly, pageable);

        if (page.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            page = findPublishedServicesPage(SupportedLanguage.EN, featuredOnly, pageable);
        }

        return PageResponse.<ServiceDTO>builder()
                .content(page.getContent().stream().map(serviceItemMapper::toDto).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDTO getPublishedServiceBySlug(String slug, SupportedLanguage language) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        return serviceItemRepository.findBySlugAndLanguageAndStatus(
                        slug,
                        safeLanguage,
                        ContentStatus.PUBLISHED
                )
                .or(() -> {
                    if (safeLanguage == SupportedLanguage.EN) {
                        return java.util.Optional.empty();
                    }

                    return serviceItemRepository.findBySlugAndLanguageAndStatus(
                            slug,
                            SupportedLanguage.EN,
                            ContentStatus.PUBLISHED
                    );
                })
                .map(serviceItemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published service not found for slug: " + slug
                ));
    }

    private Page<ServiceItem> findPublishedServicesPage(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        return featuredOnly
                ? serviceItemRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED,
                pageable
        )
                : serviceItemRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED,
                pageable
        );
    }

    private void applyCompatibleFields(ServiceDTO dto, ServiceItem entity, boolean creating) {
        entity.setName(firstNonBlank(dto.getName(), dto.getTitle(), entity.getName(), "Service name is required"));
        entity.setSlug(firstNonBlank(dto.getSlug(), entity.getSlug(), null, "Slug is required"));

        entity.setShortDescription(firstNonBlankOrNull(
                dto.getShortDescription(),
                dto.getSummary(),
                entity.getShortDescription()
        ));

        entity.setFullDescription(firstNonBlankOrNull(
                dto.getFullDescription(),
                dto.getDescription(),
                entity.getFullDescription()
        ));

        entity.setIconKey(firstNonBlankOrNull(
                dto.getIconKey(),
                dto.getIcon(),
                entity.getIconKey()
        ));

        if (StringUtils.hasText(dto.getCategory())) {
            entity.setCategory(dto.getCategory().trim());
        }

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
            entity.setStatus(creating ? ContentStatus.PUBLISHED : ContentStatus.DRAFT);
        }
    }

    private String firstNonBlank(String primary, String secondary, String existing, String errorMessage) {
        String value = firstNonBlankOrNull(primary, secondary, existing);

        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(errorMessage);
        }

        return value.trim();
    }

    private String firstNonBlankOrNull(String primary, String secondary, String existing) {
        if (StringUtils.hasText(primary)) {
            return primary.trim();
        }

        if (StringUtils.hasText(secondary)) {
            return secondary.trim();
        }

        if (StringUtils.hasText(existing)) {
            return existing.trim();
        }

        return null;
    }
}