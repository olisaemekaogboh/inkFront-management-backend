package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.TestimonialDTO;
import com.inkFront.inFront.entity.Testimonial;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.TestimonialMapper;
import com.inkFront.inFront.repository.TestimonialRepository;
import com.inkFront.inFront.service.content.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return testimonialRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(testimonialMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TestimonialDTO getById(Long id) {
        Testimonial entity = testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found with id: " + id));
        return testimonialMapper.toDto(entity);
    }

    @Override
    public TestimonialDTO create(TestimonialDTO dto) {
        Testimonial entity = testimonialMapper.toEntity(dto);

        if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }

        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
        }

        Testimonial saved = testimonialRepository.save(entity);
        return testimonialMapper.toDto(saved);
    }

    @Override
    public TestimonialDTO update(Long id, TestimonialDTO dto) {
        Testimonial entity = testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found with id: " + id));

        testimonialMapper.updateEntityFromDto(dto, entity);

        Testimonial saved = testimonialRepository.save(entity);
        return testimonialMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!testimonialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Testimonial not found with id: " + id);
        }
        testimonialRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestimonialDTO> getPublishedTestimonials(SupportedLanguage language, boolean featuredOnly, Pageable pageable) {
        Page<Testimonial> page = featuredOnly
                ? testimonialRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                language, ContentStatus.PUBLISHED, pageable
        )
                : testimonialRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language, ContentStatus.PUBLISHED, pageable
        );

        return page.map(testimonialMapper::toDto);
    }
}