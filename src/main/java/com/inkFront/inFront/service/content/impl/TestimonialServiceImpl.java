package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.TestimonialDTO;
import com.inkFront.inFront.entity.Testimonial;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.InvalidRequestException;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.TestimonialMapper;
import com.inkFront.inFront.repository.TestimonialRepository;
import com.inkFront.inFront.service.content.TestimonialService;
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
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final TestimonialMapper testimonialMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TestimonialDTO> getAll() {
        return testimonialRepository
                .findAll(Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by("id")))
                .stream()
                .map(testimonialMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TestimonialDTO getById(Long id) {
        return testimonialMapper.toDto(findById(id));
    }

    @Override
    public TestimonialDTO create(TestimonialDTO dto) {
        Testimonial entity = new Testimonial();
        applyFields(dto, entity, true);
        return testimonialMapper.toDto(testimonialRepository.save(entity));
    }

    @Override
    public TestimonialDTO update(Long id, TestimonialDTO dto) {
        Testimonial entity = findById(id);
        applyFields(dto, entity, false);
        return testimonialMapper.toDto(testimonialRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        testimonialRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestimonialDTO> getPublishedTestimonials(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        Page<Testimonial> page = findPublishedTestimonials(safeLanguage, featuredOnly, pageable);

        if (page.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            page = findPublishedTestimonials(SupportedLanguage.EN, featuredOnly, pageable);
        }

        return page.map(testimonialMapper::toDto);
    }

    private Testimonial findById(Long id) {
        return testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Testimonial not found with id: " + id
                ));
    }

    private Page<Testimonial> findPublishedTestimonials(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        if (featuredOnly) {
            return testimonialRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                    language,
                    ContentStatus.PUBLISHED,
                    pageable
            );
        }

        return testimonialRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED,
                pageable
        );
    }

    private void applyFields(TestimonialDTO dto, Testimonial entity, boolean creating) {
        if (dto == null) {
            throw new InvalidRequestException("Testimonial data is required");
        }

        entity.setClientName(required(
                first(dto.getClientName(), entity.getClientName()),
                "Client name is required"
        ));

        entity.setQuote(required(
                first(dto.getQuote(), entity.getQuote()),
                "Quote is required"
        ));

        entity.setClientRole(first(
                dto.getClientRole(),
                entity.getClientRole()
        ));

        entity.setOrganization(first(
                dto.getOrganization(),
                dto.getCompanyName(),
                entity.getOrganization()
        ));

        entity.setAvatarUrl(first(
                dto.getAvatarUrl(),
                entity.getAvatarUrl()
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