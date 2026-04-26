package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.FaqDTO;
import com.inkFront.inFront.dto.content.SiteSettingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PublicSiteContentService {

    List<SiteSettingDTO> getSiteSettings(String language, String group);

    Map<String, String> getSiteSettingsAsMap(String language, String group);

    Page<FaqDTO> getFaqs(String language, String pageKey, Pageable pageable);
}