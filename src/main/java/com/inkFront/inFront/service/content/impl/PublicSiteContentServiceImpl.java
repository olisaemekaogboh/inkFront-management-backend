package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.FaqDTO;
import com.inkFront.inFront.dto.content.SiteSettingDTO;
import com.inkFront.inFront.entity.Faq;
import com.inkFront.inFront.entity.SiteSetting;
import com.inkFront.inFront.repository.FaqRepository;
import com.inkFront.inFront.repository.SiteSettingRepository;
import com.inkFront.inFront.service.content.PublicSiteContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicSiteContentServiceImpl implements PublicSiteContentService {

    private static final String DEFAULT_LANGUAGE = "EN";
    private static final String PUBLISHED = "PUBLISHED";

    private final SiteSettingRepository siteSettingRepository;
    private final FaqRepository faqRepository;

    @Override
    public List<SiteSettingDTO> getSiteSettings(String language, String group) {
        String safeLanguage = normalize(language, DEFAULT_LANGUAGE);
        String safeGroup = normalize(group, "GENERAL");

        List<SiteSetting> settings = findPublishedSettings(safeLanguage, safeGroup);

        if (settings.isEmpty() && !DEFAULT_LANGUAGE.equalsIgnoreCase(safeLanguage)) {
            settings = findPublishedSettings(DEFAULT_LANGUAGE, safeGroup);
        }

        return settings.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Map<String, String> getSiteSettingsAsMap(String language, String group) {
        Map<String, String> result = new LinkedHashMap<>();

        for (SiteSettingDTO setting : getSiteSettings(language, group)) {
            if (setting.getSettingKey() != null) {
                result.put(setting.getSettingKey(), setting.getSettingValue());
            }
        }

        return result;
    }

    @Override
    public Page<FaqDTO> getFaqs(String language, String pageKey, Pageable pageable) {
        String safeLanguage = normalize(language, DEFAULT_LANGUAGE);
        String safePageKey = normalize(pageKey, "GENERAL");

        Page<Faq> page = findPublishedFaqs(safeLanguage, safePageKey, pageable);

        if (page.isEmpty() && !DEFAULT_LANGUAGE.equalsIgnoreCase(safeLanguage)) {
            page = findPublishedFaqs(DEFAULT_LANGUAGE, safePageKey, pageable);
        }

        return page.map(this::toDto);
    }

    private List<SiteSetting> findPublishedSettings(String language, String group) {
        return siteSettingRepository
                .findByLanguageIgnoreCaseAndSettingGroupIgnoreCaseAndStatusIgnoreCaseOrderByDisplayOrderAscSettingKeyAsc(
                        language,
                        group,
                        PUBLISHED
                );
    }

    private Page<Faq> findPublishedFaqs(String language, String pageKey, Pageable pageable) {
        return faqRepository
                .findByLanguageIgnoreCaseAndPageKeyIgnoreCaseAndStatusIgnoreCaseOrderByDisplayOrderAscIdAsc(
                        language,
                        pageKey,
                        PUBLISHED,
                        pageable
                );
    }

    private SiteSettingDTO toDto(SiteSetting setting) {
        if (setting == null) {
            return null;
        }

        return SiteSettingDTO.builder()
                .id(setting.getId())
                .settingGroup(setting.getSettingGroup())
                .settingKey(setting.getSettingKey())
                .settingValue(setting.getSettingValue())
                .valueType(setting.getValueType())
                .language(setting.getLanguage())
                .status(setting.getStatus())
                .displayOrder(setting.getDisplayOrder())
                .build();
    }

    private FaqDTO toDto(Faq faq) {
        if (faq == null) {
            return null;
        }

        return FaqDTO.builder()
                .id(faq.getId())
                .pageKey(faq.getPageKey())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .language(faq.getLanguage())
                .status(faq.getStatus())
                .displayOrder(faq.getDisplayOrder())
                .build();
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value.trim().toUpperCase();
    }
}