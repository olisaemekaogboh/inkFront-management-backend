package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.NewsletterCampaignDTO;
import com.inkFront.inFront.dto.content.NewsletterCampaignRequestDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberRequestDTO;
import com.inkFront.inFront.entity.enums.NewsletterCampaignStatus;
import com.inkFront.inFront.entity.enums.NewsletterSubscriberStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;

public interface NewsletterService {

    NewsletterSubscriberDTO subscribe(NewsletterSubscriberRequestDTO request);

    NewsletterSubscriberDTO unsubscribe(String token);

    Page<NewsletterSubscriberDTO> findSubscribers(
            SupportedLanguage language,
            NewsletterSubscriberStatus status,
            String search,
            int page,
            int size
    );

    NewsletterCampaignDTO createCampaign(NewsletterCampaignRequestDTO request);

    NewsletterCampaignDTO updateCampaign(Long id, NewsletterCampaignRequestDTO request);

    void deleteCampaign(Long id);

    NewsletterCampaignDTO sendCampaign(Long id);

    NewsletterCampaignDTO archiveCampaign(Long id);

    NewsletterCampaignDTO getCampaign(Long id);

    Page<NewsletterCampaignDTO> findCampaigns(
            SupportedLanguage language,
            NewsletterCampaignStatus status,
            String search,
            int page,
            int size
    );
}