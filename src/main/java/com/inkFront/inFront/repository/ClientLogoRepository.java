package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.ClientLogo;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLogoRepository extends JpaRepository<ClientLogo, Long> {

    Page<ClientLogo> findByLanguageAndStatusOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status,
            Pageable pageable
    );

    Page<ClientLogo> findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
            SupportedLanguage language,
            ContentStatus status,
            Pageable pageable
    );
}