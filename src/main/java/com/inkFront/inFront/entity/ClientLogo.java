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
        name = "client_logos",
        indexes = {
                @Index(name = "idx_client_logos_language_status", columnList = "language,status"),
                @Index(name = "idx_client_logos_display_order", columnList = "displayOrder")
        }
)
public class ClientLogo extends AuditableEntity {

    @Column(nullable = false, length = 160)
    private String name;

    @Column(nullable = false, length = 255)
    private String logoUrl;

    @Column(length = 255)
    private String websiteUrl;

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