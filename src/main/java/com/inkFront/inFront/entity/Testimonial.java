package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.AuditableEntity;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "testimonials",
        indexes = {
                @Index(name = "idx_testimonials_language_status", columnList = "language,status"),
                @Index(name = "idx_testimonials_display_order", columnList = "displayOrder")
        }
)
public class Testimonial extends AuditableEntity {

    @Column(nullable = false, length = 120)
    private String clientName;

    @Column(length = 120)
    private String clientRole;

    @Column(length = 120)
    private String organization;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String quote;

    @Column(length = 255)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupportedLanguage language = SupportedLanguage.EN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContentStatus status = ContentStatus.DRAFT;

    @Column(nullable = false)
    private Boolean featured = false;

    @Column(nullable = false)
    private Integer displayOrder = 0;
}