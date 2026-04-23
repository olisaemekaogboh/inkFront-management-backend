package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.api.PageResponse;
import com.inkFront.inFront.dto.content.ServiceDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceItemService {

    List<ServiceDTO> getAll();

    ServiceDTO getById(Long id);

    ServiceDTO create(ServiceDTO dto);

    ServiceDTO update(Long id, ServiceDTO dto);

    void delete(Long id);

    PageResponse<ServiceDTO> getPublishedServices(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    );

    ServiceDTO getPublishedServiceBySlug(String slug, SupportedLanguage language);
}