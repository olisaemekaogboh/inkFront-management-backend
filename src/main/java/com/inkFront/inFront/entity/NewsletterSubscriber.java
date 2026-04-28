package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.BaseEntity;
import com.inkFront.inFront.entity.enums.NewsletterSubscriberStatus;
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
@Table(name = "newsletter_subscribers")
public class NewsletterSubscriber extends BaseEntity {

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(length = 120)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupportedLanguage language = SupportedLanguage.EN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NewsletterSubscriberStatus status = NewsletterSubscriberStatus.ACTIVE;

    @Column(nullable = false, unique = true, length = 80)
    private String unsubscribeToken;

    private LocalDateTime subscribedAt;

    private LocalDateTime unsubscribedAt;
}