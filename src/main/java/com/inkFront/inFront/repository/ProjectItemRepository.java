package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.ProjectItem;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

    List<ProjectItem> findByLanguageAndStatusOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status
    );

    List<ProjectItem> findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status
    );

    Optional<ProjectItem> findBySlugAndLanguageAndStatus(
            String slug,
            SupportedLanguage language,
            ContentStatus status
    );

    Optional<ProjectItem> findByIdAndLanguageAndStatus(
            Long id,
            SupportedLanguage language,
            ContentStatus status
    );

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}