package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.NewsletterCampaignDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberDTO;
import com.inkFront.inFront.entity.NewsletterCampaign;
import com.inkFront.inFront.entity.NewsletterSubscriber;
import org.springframework.stereotype.Component;

@Component
public class NewsletterMapper {

    public NewsletterSubscriberDTO toSubscriberDTO(NewsletterSubscriber subscriber) {
        if (subscriber == null) {
            return null;
        }

        NewsletterSubscriberDTO dto = new NewsletterSubscriberDTO();
        dto.setId(subscriber.getId());
        dto.setEmail(subscriber.getEmail());
        dto.setFullName(subscriber.getFullName());
        dto.setLanguage(subscriber.getLanguage());
        dto.setStatus(subscriber.getStatus());
        dto.setSubscribedAt(subscriber.getSubscribedAt());
        dto.setUnsubscribedAt(subscriber.getUnsubscribedAt());

        return dto;
    }

    public NewsletterCampaignDTO toCampaignDTO(NewsletterCampaign campaign) {
        if (campaign == null) {
            return null;
        }

        NewsletterCampaignDTO dto = new NewsletterCampaignDTO();
        dto.setId(campaign.getId());
        dto.setSubject(campaign.getSubject());
        dto.setPreviewText(campaign.getPreviewText());
        dto.setContent(campaign.getContent());
        dto.setImageUrl(campaign.getImageUrl());
        dto.setCtaLabel(campaign.getCtaLabel());
        dto.setCtaUrl(campaign.getCtaUrl());
        dto.setLanguage(campaign.getLanguage());
        dto.setStatus(campaign.getStatus());
        dto.setSentAt(campaign.getSentAt());
        dto.setSentCount(campaign.getSentCount());

        return dto;
    }
}