package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.HeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/hero-sections")
@RequiredArgsConstructor
public class PublicHeroSectionController {

    private final HeroSectionService heroSectionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HeroSectionDTO>>> getPublishedHeroSections(
            @RequestParam SupportedLanguage language,
            @RequestParam String placement,
            @RequestParam(defaultValue = "false") boolean featuredOnly
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        heroSectionService.getPublishedHeroSections(language, placement, featuredOnly)
                )
        );
    }
}