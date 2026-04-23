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
            @RequestParam SupportedLanguage language,
            @RequestParam(defaultValue = "false") boolean featuredOnly
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
            @RequestParam SupportedLanguage language
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productBlueprintService.getPublishedProductBlueprintBySlug(slug, language)
                )
        );
    }
}