package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;

import java.util.List;

public interface HomepageSectionService {

    List<HomepageSectionDTO> getAll();

    HomepageSectionDTO getById(Long id);

    HomepageSectionDTO create(HomepageSectionDTO dto);

    HomepageSectionDTO update(Long id, HomepageSectionDTO dto);

    void delete(Long id);

    List<HomepageSectionDTO> getPublishedSections(SupportedLanguage language);

    HomepageSectionDTO getPublishedSectionByKey(String sectionKey, SupportedLanguage language);
}