package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.ProductBlueprintDTO;
import com.inkFront.inFront.entity.ProductBlueprint;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.DuplicateResourceException;
import com.inkFront.inFront.exception.InvalidRequestException;
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

    private final ProductBlueprintRepository repository;
    private final ProductBlueprintMapper mapper;

    @Override
    public List<ProductBlueprintDTO> getAll() {
        return repository.findAll(Sort.by("displayOrder"))
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ProductBlueprintDTO getById(Long id) {
        return mapper.toDto(findById(id));
    }

    @Override
    public ProductBlueprintDTO create(ProductBlueprintDTO dto) {
        ProductBlueprint entity = new ProductBlueprint();
        applyFields(dto, entity, true);
        validateSlug(entity.getSlug(), entity.getLanguage(), null);

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ProductBlueprintDTO update(Long id, ProductBlueprintDTO dto) {
        ProductBlueprint entity = findById(id);
        applyFields(dto, entity, false);
        validateSlug(entity.getSlug(), entity.getLanguage(), id);

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public List<ProductBlueprintDTO> getPublishedProductBlueprints(SupportedLanguage language, boolean featuredOnly) {
        SupportedLanguage lang = language == null ? SupportedLanguage.EN : language;

        List<ProductBlueprint> list = featuredOnly
                ? repository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(lang, ContentStatus.PUBLISHED)
                : repository.findByLanguageAndStatusOrderByDisplayOrderAsc(lang, ContentStatus.PUBLISHED);

        return list.stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductBlueprintDTO getPublishedProductBlueprintBySlug(
            String slug,
            SupportedLanguage language
    ) {
        SupportedLanguage lang = language == null ? SupportedLanguage.EN : language;

        ProductBlueprint entity = repository
                .findBySlugAndLanguageAndStatus(slug, lang, ContentStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return mapper.toDto(entity);
    }

    private ProductBlueprint findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    private void validateSlug(String slug, SupportedLanguage lang, Long id) {
        boolean exists = id == null
                ? repository.existsBySlugAndLanguage(slug, lang)
                : repository.existsBySlugAndLanguageAndIdNot(slug, lang, id);

        if (exists) {
            throw new DuplicateResourceException("Slug exists for language");
        }
    }

    private void applyFields(ProductBlueprintDTO dto, ProductBlueprint e, boolean creating) {

        e.setTitle(required(dto.getTitle(), "Title required"));
        e.setSlug(required(dto.getSlug(), "Slug required"));

        e.setSummary(first(dto.getSummary(), dto.getShortDescription()));
        e.setDescription(first(dto.getDescription(), dto.getFullDescription()));

        e.setChallengeStatement(dto.getChallengeStatement());
        e.setSolutionOverview(dto.getSolutionOverview());
        e.setFeatureHighlights(dto.getFeatureHighlights());

        e.setHeroImageUrl(first(dto.getHeroImageUrl(), dto.getImageUrl()));
        e.setImageUrl(first(dto.getImageUrl(), dto.getHeroImageUrl()));

        e.setLanguage(dto.getLanguage() != null ? dto.getLanguage() : SupportedLanguage.EN);

        e.setDisplayOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        e.setFeatured(Boolean.TRUE.equals(dto.getFeatured()));

        if (dto.getActive() != null) {
            e.setStatus(dto.getActive() ? ContentStatus.PUBLISHED : ContentStatus.DRAFT);
        } else if (dto.getStatus() != null) {
            e.setStatus(dto.getStatus());
        } else if (creating) {
            e.setStatus(ContentStatus.DRAFT);
        }
    }

    private String first(String... vals) {
        for (String v : vals) {
            if (StringUtils.hasText(v)) return v.trim();
        }
        return null;
    }

    private String required(String v, String msg) {
        if (!StringUtils.hasText(v)) throw new InvalidRequestException(msg);
        return v.trim();
    }
}