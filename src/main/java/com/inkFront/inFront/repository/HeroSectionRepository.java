package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.HeroSection;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroSectionRepository extends JpaRepository<HeroSection, Long> {

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