package com.inkFront.inFront.repository;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.HeroSection;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HeroSectionRepository extends JpaRepository<HeroSection, Long> {

    // DTO projection interface for better performance
    interface HeroSectionProjection {
        Long getId();
        String getTitle();
        String getSubtitle();
        String getBody();
        String getBackgroundImageUrl();
        String getPlacement();
        String getPrimaryButtonLabel();
        String getPrimaryButtonUrl();
        String getSecondaryButtonLabel();
        String getSecondaryButtonUrl();
        SupportedLanguage getLanguage();
        ContentStatus getStatus();
        Boolean getFeatured();
        Integer getDisplayOrder();
    }

    // Use JPQL with constructor expression for DTO
    @Query("SELECT NEW com.inkFront.inFront.dto.content.HeroSectionDTO(" +
            "h.id, h.title, h.subtitle, h.body, h.backgroundImageUrl, " +
            "h.placement, h.primaryButtonLabel, h.primaryButtonUrl, " +
            "h.secondaryButtonLabel, h.secondaryButtonUrl, " +
            "h.language, h.status, h.featured, h.displayOrder) " +
            "FROM HeroSection h " +
            "WHERE h.language = :language " +
            "AND h.placement = :placement " +
            "AND h.status = :status " +
            "ORDER BY h.displayOrder ASC")
    List<HeroSectionDTO> findPublishedHeroSectionsDTO(
            @Param("language") SupportedLanguage language,
            @Param("placement") String placement,
            @Param("status") ContentStatus status
    );

    @Query("SELECT NEW com.inkFront.inFront.dto.content.HeroSectionDTO(" +
            "h.id, h.title, h.subtitle, h.body, h.backgroundImageUrl, " +
            "h.placement, h.primaryButtonLabel, h.primaryButtonUrl, " +
            "h.secondaryButtonLabel, h.secondaryButtonUrl, " +
            "h.language, h.status, h.featured, h.displayOrder) " +
            "FROM HeroSection h " +
            "WHERE h.language = :language " +
            "AND h.placement = :placement " +
            "AND h.status = :status " +
            "AND h.featured = true " +
            "ORDER BY h.displayOrder ASC")
    List<HeroSectionDTO> findFeaturedPublishedHeroSectionsDTO(
            @Param("language") SupportedLanguage language,
            @Param("placement") String placement,
            @Param("status") ContentStatus status
    );

    // Keep original methods for admin CRUD
    List<HeroSection> findByLanguageAndPlacementAndStatusOrderByDisplayOrderAsc(
            SupportedLanguage language,
            String placement,
            ContentStatus status
    );

    List<HeroSection> findByLanguageAndPlacementAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
            SupportedLanguage language,
            String placement,
            ContentStatus status
    );
}