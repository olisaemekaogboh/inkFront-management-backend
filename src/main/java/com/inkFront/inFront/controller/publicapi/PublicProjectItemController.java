package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.ProjectItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/portfolio-projects")
@RequiredArgsConstructor
public class PublicProjectItemController {

    private final ProjectItemService projectItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getPublishedProjects(
            @RequestParam(defaultValue = "EN") SupportedLanguage language,
            @RequestParam(defaultValue = "false") boolean featuredOnly
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        projectItemService.getPublishedProjects(language, featuredOnly)
                )
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProjectDTO>> getPublishedProjectBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "EN") SupportedLanguage language
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        projectItemService.getPublishedProjectBySlug(slug, language)
                )
        );
    }
}