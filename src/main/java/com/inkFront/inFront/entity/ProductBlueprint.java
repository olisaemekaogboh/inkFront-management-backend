package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.SluggableEntity;
import com.inkFront.inFront.entity.enums.ContentStatus;

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
@Table(name = "product_blueprints")
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

    @Column(length = 255)
    private String heroImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SupportedLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContentStatus status = ContentStatus.DRAFT;

    @Column(nullable = false)
    private Boolean featured = false;

    @Column(nullable = false)
    private Integer displayOrder = 0;
}