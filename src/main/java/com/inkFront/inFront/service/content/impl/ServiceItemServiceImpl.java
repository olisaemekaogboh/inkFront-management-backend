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

        if (!StringUtils.hasText(entity.getSlug())) {
            throw new IllegalArgumentException("Slug is required");
        }

        if (serviceItemRepository.existsBySlug(entity.getSlug())) {
            throw new IllegalArgumentException("Service slug already exists: " + entity.getSlug());
        }

        if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }

        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
        }

        ServiceItem saved = serviceItemRepository.save(entity);
        return serviceItemMapper.toDto(saved);
    }

    @Override
    public ServiceDTO update(Long id, ServiceDTO dto) {
        ServiceItem entity = serviceItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service item not found with id: " + id));

        serviceItemMapper.updateEntityFromDto(dto, entity);

        if (!StringUtils.hasText(entity.getSlug())) {
            throw new IllegalArgumentException("Slug is required");
        }

        if (serviceItemRepository.existsBySlugAndIdNot(entity.getSlug(), id)) {
            throw new IllegalArgumentException("Service slug already exists: " + entity.getSlug());
        }

        if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }

        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
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
        Page<ServiceItem> page = featuredOnly
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
        ServiceItem entity = serviceItemRepository.findBySlugAndLanguageAndStatus(
                        slug,
                        language,
                        ContentStatus.PUBLISHED
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published service not found for slug: " + slug + " and language: " + language
                ));

        return serviceItemMapper.toDto(entity);
    }
}