package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.ProductBlueprint;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductBlueprintRepository extends JpaRepository<ProductBlueprint, Long> {

    List<ProductBlueprint> findByLanguageAndStatusOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status
    );

    List<ProductBlueprint> findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status
    );

    Optional<ProductBlueprint> findBySlugAndLanguageAndStatus(
            String slug,
            SupportedLanguage language,
            ContentStatus status
    );

    boolean existsBySlugAndLanguage(String slug, SupportedLanguage language);

    boolean existsBySlugAndLanguageAndIdNot(
            String slug,
            SupportedLanguage language,
            Long id
    );
}