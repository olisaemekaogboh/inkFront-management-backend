package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.entity.enums.NewsletterSubscriberStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;

import java.time.LocalDateTime;

public class NewsletterSubscriberDTO {

    private Long id;
    private String email;
    private String fullName;
    private SupportedLanguage language;
    private NewsletterSubscriberStatus status;
    private LocalDateTime subscribedAt;
    private LocalDateTime unsubscribedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public SupportedLanguage getLanguage() { return language; }
    public void setLanguage(SupportedLanguage language) { this.language = language; }

    public NewsletterSubscriberStatus getStatus() { return status; }
    public void setStatus(NewsletterSubscriberStatus status) { this.status = status; }

    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }

    public LocalDateTime getUnsubscribedAt() { return unsubscribedAt; }
    public void setUnsubscribedAt(LocalDateTime unsubscribedAt) { this.unsubscribedAt = unsubscribedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}