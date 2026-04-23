package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.ProductBlueprintDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;

import java.util.List;

public interface ProductBlueprintService {

    List<ProductBlueprintDTO> getAll();

    ProductBlueprintDTO getById(Long id);

    ProductBlueprintDTO create(ProductBlueprintDTO dto);

    ProductBlueprintDTO update(Long id, ProductBlueprintDTO dto);

    void delete(Long id);

    List<ProductBlueprintDTO> getPublishedProductBlueprints(SupportedLanguage language, boolean featuredOnly);

    ProductBlueprintDTO getPublishedProductBlueprintBySlug(String slug, SupportedLanguage language);
}