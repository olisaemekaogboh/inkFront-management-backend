package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.ProductBlueprintDTO;
import com.inkFront.inFront.entity.ProductBlueprint;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.ProductBlueprintMapper;
import com.inkFront.inFront.repository.ProductBlueprintRepository;
import com.inkFront.inFront.service.content.ProductBlueprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductBlueprintServiceImpl implements ProductBlueprintService {

    private final ProductBlueprintRepository productBlueprintRepository;
    private final ProductBlueprintMapper productBlueprintMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductBlueprintDTO> getAll() {
        return productBlueprintRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(productBlueprintMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBlueprintDTO getById(Long id) {
        ProductBlueprint entity = productBlueprintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product blueprint not found with id: " + id));

        return productBlueprintMapper.toDto(entity);
    }

    @Override
    public ProductBlueprintDTO create(ProductBlueprintDTO dto) {
        ProductBlueprint entity = productBlueprintMapper.toEntity(dto);

        if (!StringUtils.hasText(entity.getSlug())) {
            throw new IllegalArgumentException("Slug is required");
        }

        if (productBlueprintRepository.existsBySlug(entity.getSlug())) {
            throw new IllegalArgumentException("Product blueprint slug already exists: " + entity.getSlug());
        }

        applyDefaults(entity);

        ProductBlueprint saved = productBlueprintRepository.save(entity);
        return productBlueprintMapper.toDto(saved);
    }

    @Override
    public ProductBlueprintDTO update(Long id, ProductBlueprintDTO dto) {
        ProductBlueprint entity = productBlueprintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product blueprint not found with id: " + id));

        productBlueprintMapper.updateEntityFromDto(dto, entity);

        if (!StringUtils.hasText(entity.getSlug())) {
            throw new IllegalArgumentException("Slug is required");
        }

        if (productBlueprintRepository.existsBySlugAndIdNot(entity.getSlug(), id)) {
            throw new IllegalArgumentException("Product blueprint slug already exists: " + entity.getSlug());
        }

        applyDefaults(entity);

        ProductBlueprint saved = productBlueprintRepository.save(entity);
        return productBlueprintMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!productBlueprintRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product blueprint not found with id: " + id);
        }

        productBlueprintRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBlueprintDTO> getPublishedProductBlueprints(
            SupportedLanguage language,
            boolean featuredOnly
    ) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        List<ProductBlueprint> items = findPublishedProductBlueprints(safeLanguage, featuredOnly);

        if (items.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            items = findPublishedProductBlueprints(SupportedLanguage.EN, featuredOnly);
        }

        return items.stream()
                .map(productBlueprintMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBlueprintDTO getPublishedProductBlueprintBySlug(String slug, SupportedLanguage language) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        return productBlueprintRepository.findBySlugAndLanguageAndStatus(
                        slug,
                        safeLanguage,
                        ContentStatus.PUBLISHED
                )
                .or(() -> {
                    if (safeLanguage == SupportedLanguage.EN) {
                        return java.util.Optional.empty();
                    }

                    return productBlueprintRepository.findBySlugAndLanguageAndStatus(
                            slug,
                            SupportedLanguage.EN,
                            ContentStatus.PUBLISHED
                    );
                })
                .map(productBlueprintMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published product blueprint not found for slug: " + slug
                ));
    }

    private List<ProductBlueprint> findPublishedProductBlueprints(
            SupportedLanguage language,
            boolean featuredOnly
    ) {
        return featuredOnly
                ? productBlueprintRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED
        )
                : productBlueprintRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED
        );
    }

    private void applyDefaults(ProductBlueprint entity) {
        if (entity.getLanguage() == null) {
            entity.setLanguage(SupportedLanguage.EN);
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
    }
}