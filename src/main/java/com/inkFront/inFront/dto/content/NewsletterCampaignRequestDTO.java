package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.entity.enums.SupportedLanguage;

public class NewsletterCampaignRequestDTO {

    private String subject;
    private String previewText;
    private String content;
    private String imageUrl;
    private String ctaLabel;
    private String ctaUrl;
    private SupportedLanguage language;

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getPreviewText() { return previewText; }
    public void setPreviewText(String previewText) { this.previewText = previewText; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCtaLabel() { return ctaLabel; }
    public void setCtaLabel(String ctaLabel) { this.ctaLabel = ctaLabel; }

    public String getCtaUrl() { return ctaUrl; }
    public void setCtaUrl(String ctaUrl) { this.ctaUrl = ctaUrl; }

    public SupportedLanguage getLanguage() { return language; }
    public void setLanguage(SupportedLanguage language) { this.language = language; }
}