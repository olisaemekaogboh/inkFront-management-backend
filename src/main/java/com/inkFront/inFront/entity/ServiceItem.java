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
@Table(name = "service_items")
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
    private SupportedLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContentStatus status = ContentStatus.DRAFT;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean featured = false;
}
