package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.BaseEntity;
import com.inkFront.inFront.entity.enums.NewsletterCampaignStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "newsletter_campaigns")
public class NewsletterCampaign extends BaseEntity {

    @Column(nullable = false, length = 180)
    private String subject;

    @Column(length = 500)
    private String previewText;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 255)
    private String ctaLabel;

    @Column(length = 500)
    private String ctaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupportedLanguage language = SupportedLanguage.EN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NewsletterCampaignStatus status = NewsletterCampaignStatus.DRAFT;

    private LocalDateTime sentAt;

    private Integer sentCount = 0;
}