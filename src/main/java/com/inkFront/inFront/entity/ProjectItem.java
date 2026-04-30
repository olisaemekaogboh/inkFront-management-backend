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
        name = "project_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_items_slug_language",
                        columnNames = {"slug", "language"}
                )
        },
        indexes = {
                @Index(name = "idx_project_items_language_status", columnList = "language,status"),
                @Index(name = "idx_project_items_display_order", columnList = "displayOrder")
        }
)
public class ProjectItem extends SluggableEntity {

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 120)
    private String clientIndustry;

    @Column(length = 120)
    private String projectType;

    @Column(length = 200)
    private String clientName;

    @Column(columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String liveUrl;

    @Column(columnDefinition = "TEXT")
    private String projectUrl;

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