package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.content.FaqDTO;
import com.inkFront.inFront.dto.content.SiteSettingDTO;
import com.inkFront.inFront.service.content.PublicSiteContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicSiteContentController {

    private final PublicSiteContentService publicSiteContentService;

    @GetMapping("/site-settings")
    public Map<String, String> getSiteSettingsAsMap(
            @RequestParam(defaultValue = "EN") String language,
            @RequestParam(defaultValue = "GENERAL") String group
    ) {
        return publicSiteContentService.getSiteSettingsAsMap(language, group);
    }

    @GetMapping("/site-settings/list")
    public List<SiteSettingDTO> getSiteSettingsAsList(
            @RequestParam(defaultValue = "EN") String language,
            @RequestParam(defaultValue = "GENERAL") String group
    ) {
        return publicSiteContentService.getSiteSettings(language, group);
    }

    @GetMapping("/faqs")
    public Page<FaqDTO> getFaqs(
            @RequestParam(defaultValue = "EN") String language,
            @RequestParam(defaultValue = "GENERAL") String pageKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return publicSiteContentService.getFaqs(
                language,
                pageKey,
                PageRequest.of(safePage, safeSize)
        );
    }
}