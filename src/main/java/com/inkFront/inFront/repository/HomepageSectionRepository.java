package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.HomepageSection;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HomepageSectionRepository extends JpaRepository<HomepageSection, Long> {

    List<HomepageSection> findByLanguageAndStatusOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status
    );

    Optional<HomepageSection> findBySectionKeyIgnoreCaseAndLanguageAndStatus(
            String sectionKey,
            SupportedLanguage language,
            ContentStatus status
    );
}