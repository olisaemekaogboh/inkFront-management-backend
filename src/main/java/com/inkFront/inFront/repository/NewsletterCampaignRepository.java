package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.NewsletterCampaign;
import com.inkFront.inFront.entity.enums.NewsletterCampaignStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterCampaignRepository extends JpaRepository<NewsletterCampaign, Long> {

    Page<NewsletterCampaign> findAllByStatus(
            NewsletterCampaignStatus status,
            Pageable pageable
    );

    Page<NewsletterCampaign> findAllByLanguage(
            SupportedLanguage language,
            Pageable pageable
    );

    Page<NewsletterCampaign> findAllByLanguageAndStatus(
            SupportedLanguage language,
            NewsletterCampaignStatus status,
            Pageable pageable
    );

    Page<NewsletterCampaign> findAllBySubjectContainingIgnoreCaseOrPreviewTextContainingIgnoreCase(
            String subject,
            String previewText,
            Pageable pageable
    );
}