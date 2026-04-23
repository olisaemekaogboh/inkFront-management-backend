package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.ServiceItem;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    Page<ServiceItem> findByLanguageAndStatusOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status,
            Pageable pageable
    );

    Page<ServiceItem> findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status,
            Pageable pageable
    );

    Optional<ServiceItem> findBySlugAndLanguageAndStatus(
            String slug,
            SupportedLanguage language,
            ContentStatus status
    );

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}