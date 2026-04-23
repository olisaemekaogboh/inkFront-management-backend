package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.TestimonialDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TestimonialService {

    List<TestimonialDTO> getAll();

    TestimonialDTO getById(Long id);

    TestimonialDTO create(TestimonialDTO dto);

    TestimonialDTO update(Long id, TestimonialDTO dto);

    void delete(Long id);

    Page<TestimonialDTO> getPublishedTestimonials(SupportedLanguage language, boolean featuredOnly, Pageable pageable);
}