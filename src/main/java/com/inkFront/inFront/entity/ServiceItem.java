package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.SluggableEntity;
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
        name = "service_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_service_items_slug_language",
                        columnNames = {"slug", "language"}
                )
        },
        indexes = {
                @Index(name = "idx_service_items_language_status", columnList = "language,status"),
                @Index(name = "idx_service_items_display_order", columnList = "displayOrder")
        }
)
public class ServiceItem extends SluggableEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 200)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @Column(length = 100)
    private String category;

    @Column(length = 100)
    private String iconKey;

    @Column(length = 700)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupportedLanguage language = SupportedLanguage.EN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContentStatus status = ContentStatus.DRAFT;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean featured = false;
}