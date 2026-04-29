package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.dto.api.PageResponse;
import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.ClientLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/client-logos")
@RequiredArgsConstructor
public class PublicClientLogoController {

    private final ClientLogoService clientLogoService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ClientLogoDTO>>> getPublishedClientLogos(
            @RequestParam(defaultValue = "EN") SupportedLanguage language,
            @RequestParam(defaultValue = "false") boolean featuredOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return ResponseEntity.ok(
                ApiResponse.success(
                        clientLogoService.getPublishedClientLogos(
                                language,
                                featuredOnly,
                                PageRequest.of(safePage, safeSize)
                        )
                )
        );
    }
}