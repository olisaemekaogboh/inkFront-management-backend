package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.HomepageSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/homepage-sections")
@RequiredArgsConstructor
public class PublicHomepageSectionController {

    private final HomepageSectionService homepageSectionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HomepageSectionDTO>>> getPublishedSections(
            @RequestParam SupportedLanguage language
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        homepageSectionService.getPublishedSections(language)
                )
        );
    }

    @GetMapping("/{sectionKey}")
    public ResponseEntity<ApiResponse<HomepageSectionDTO>> getPublishedSectionByKey(
            @PathVariable String sectionKey,
            @RequestParam SupportedLanguage language
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        homepageSectionService.getPublishedSectionByKey(sectionKey, language)
                )
        );
    }
}