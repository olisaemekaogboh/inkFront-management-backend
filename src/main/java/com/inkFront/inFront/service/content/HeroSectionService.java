package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;

import java.util.List;

public interface HeroSectionService {

    List<HeroSectionDTO> getAll();

    HeroSectionDTO getById(Long id);

    HeroSectionDTO create(HeroSectionDTO dto);

    HeroSectionDTO update(Long id, HeroSectionDTO dto);

    void delete(Long id);

    List<HeroSectionDTO> getPublishedHeroSections(
            SupportedLanguage language,
            String placement,
            boolean featuredOnly
    );
}