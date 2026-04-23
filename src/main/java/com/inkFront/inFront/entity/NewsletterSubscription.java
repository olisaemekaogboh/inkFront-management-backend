package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.AuditableEntity;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "newsletter_subscriptions")
public class NewsletterSubscription extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupportedLanguage language;
}
