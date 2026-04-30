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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "homepage_sections",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_homepage_sections_key_language",
                        columnNames = {"sectionKey", "language"}
                )
        },
        indexes = {
                @Index(name = "idx_homepage_sections_language_status", columnList = "language,status"),
                @Index(name = "idx_homepage_sections_display_order", columnList = "displayOrder")
        }
)
public class HomepageSection extends AuditableEntity {

    @Column(nullable = false, length = 100)
    private String sectionKey;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(length = 700)
    private String imageUrl;

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