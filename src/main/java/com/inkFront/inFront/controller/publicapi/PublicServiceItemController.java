package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.dto.api.PageResponse;
import com.inkFront.inFront.dto.content.ServiceDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.ServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/services")
@RequiredArgsConstructor
public class PublicServiceItemController {

    private final ServiceItemService serviceItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ServiceDTO>>> getPublishedServices(
            @RequestParam(defaultValue = "EN") SupportedLanguage language,
            @RequestParam(defaultValue = "false") boolean featuredOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        Pageable pageable = PageRequest.of(safePage, safeSize);

        return ResponseEntity.ok(
                ApiResponse.success(
                        serviceItemService.getPublishedServices(language, featuredOnly, pageable)
                )
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ServiceDTO>> getPublishedServiceBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "EN") SupportedLanguage language
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        serviceItemService.getPublishedServiceBySlug(slug, language)
                )
        );
    }
}