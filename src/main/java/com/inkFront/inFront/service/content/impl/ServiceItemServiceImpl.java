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

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceItemServiceImpl implements ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final ServiceItemMapper serviceItemMapper;

    @Override
    @Transactional(readOnly = true)
    public java.util.List<ServiceDTO> getAll() {
        return serviceItemRepository
                .findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(serviceItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDTO getById(Long id) {
        return serviceItemMapper.toDto(findById(id));
    }

    @Override
    public ServiceDTO create(ServiceDTO dto) {
        ServiceItem entity = new ServiceItem();
        applyFields(dto, entity, true);
        validateSlugLanguageUnique(entity.getSlug(), entity.getLanguage(), null);

        return serviceItemMapper.toDto(serviceItemRepository.save(entity));
    }

    @Override
    public ServiceDTO update(Long id, ServiceDTO dto) {
        ServiceItem entity = findById(id);

        applyFields(dto, entity, false);
        validateSlugLanguageUnique(entity.getSlug(), entity.getLanguage(), id);

        return serviceItemMapper.toDto(serviceItemRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        ServiceItem entity = findById(id);
        serviceItemRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ServiceDTO> getPublishedServices(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        Page<ServiceItem> page = findPublishedServicesPage(
                safeLanguage,
                featuredOnly,
                pageable
        );

        if (page.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            page = findPublishedServicesPage(
                    SupportedLanguage.EN,
                    featuredOnly,
                    pageable
            );
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

        return serviceItemRepository
                .findBySlugAndLanguageAndStatus(slug, safeLanguage, ContentStatus.PUBLISHED)
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

    private ServiceItem findById(Long id) {
        return serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service item not found with id: " + id
                ));
    }

    private Page<ServiceItem> findPublishedServicesPage(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        if (featuredOnly) {
            return serviceItemRepository
                    .findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                            language,
                            ContentStatus.PUBLISHED,
                            pageable
                    );
        }

        return serviceItemRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED,
                pageable
        );
    }

    private void applyFields(ServiceDTO dto, ServiceItem entity, boolean creating) {
        if (dto == null) {
            throw new IllegalArgumentException("Service data is required");
        }

        entity.setName(required(
                first(dto.getName(), dto.getTitle(), entity.getName()),
                "Service name is required"
        ));

        entity.setSlug(required(
                first(dto.getSlug(), entity.getSlug()),
                "Slug is required"
        ));

        entity.setShortDescription(first(
                dto.getShortDescription(),
                dto.getSummary(),
                entity.getShortDescription()
        ));

        entity.setFullDescription(first(
                dto.getFullDescription(),
                dto.getDescription(),
                entity.getFullDescription()
        ));

        entity.setIconKey(first(
                dto.getIconKey(),
                dto.getIcon(),
                entity.getIconKey()
        ));

        entity.setCategory(first(
                dto.getCategory(),
                entity.getCategory()
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
            entity.setStatus(creating ? ContentStatus.PUBLISHED : ContentStatus.DRAFT);
        }
    }

    private void validateSlugLanguageUnique(
            String slug,
            SupportedLanguage language,
            Long currentId
    ) {
        boolean exists;

        if (currentId == null) {
            exists = serviceItemRepository.existsBySlugAndLanguage(slug, language);
        } else {
            exists = serviceItemRepository.existsBySlugAndLanguageAndIdNot(
                    slug,
                    language,
                    currentId
            );
        }

        if (exists) {
            throw new IllegalArgumentException(
                    "Service slug already exists for language " + language + ": " + slug
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
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}