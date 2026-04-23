package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.api.PageResponse;
import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientLogoService {

    List<ClientLogoDTO> getAll();

    ClientLogoDTO getById(Long id);

    ClientLogoDTO create(ClientLogoDTO dto);

    ClientLogoDTO update(Long id, ClientLogoDTO dto);

    void delete(Long id);

    PageResponse<ClientLogoDTO> getPublishedClientLogos(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    );
}