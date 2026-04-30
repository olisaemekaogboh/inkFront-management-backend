package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.SluggableEntity;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "product_blueprints",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_blueprints_slug_language",
                        columnNames = {"slug", "language"}
                )
        }
)
public class ProductBlueprint extends SluggableEntity {

    @Column(nullable = false, length = 180)
    private String title;

    @Column(nullable = false, length = 240)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String challengeStatement;

    @Column(columnDefinition = "TEXT")
    private String solutionOverview;

    @Column(columnDefinition = "TEXT")
    private String featureHighlights;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String heroImageUrl;

    @Column(length = 255)
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