package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.dto.content.ProductBlueprintDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.ProductBlueprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/product-blueprints")
@RequiredArgsConstructor
public class PublicProductBlueprintController {

    private final ProductBlueprintService productBlueprintService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductBlueprintDTO>>> getPublishedProductBlueprints(
            @RequestParam(defaultValue = "EN") SupportedLanguage language,
            @RequestParam(defaultValue = "false") boolean featuredOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productBlueprintService.getPublishedProductBlueprints(language, featuredOnly)
                )
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductBlueprintDTO>> getPublishedProductBlueprintBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "EN") SupportedLanguage language
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productBlueprintService.getPublishedProductBlueprintBySlug(slug, language)
                )
        );
    }
}