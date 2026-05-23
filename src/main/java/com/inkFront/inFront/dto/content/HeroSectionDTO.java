package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.AuditDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeroSectionDTO extends AuditDTO {

    private String title;
    private String subtitle;
    private String body;
    private String description;
    private String backgroundImageUrl;
    private String imageUrl;
    private String placement;
    private String primaryButtonLabel;
    private String primaryButtonUrl;
    private String secondaryButtonLabel;
    private String secondaryButtonUrl;
    private SupportedLanguage language;
    private ContentStatus status;
    private Boolean featured;
    private Boolean active;
    private Integer displayOrder;
    private Integer sortOrder;

    // Constructor for JPQL projection
    public HeroSectionDTO(Long id, String title, String subtitle, String body,
                          String backgroundImageUrl, String placement,
                          String primaryButtonLabel, String primaryButtonUrl,
                          String secondaryButtonLabel, String secondaryButtonUrl,
                          SupportedLanguage language, ContentStatus status,
                          Boolean featured, Integer displayOrder) {
        this.setId(id);
        this.title = title;
        this.subtitle = subtitle;
        this.body = body;
        this.backgroundImageUrl = backgroundImageUrl;
        this.placement = placement;
        this.primaryButtonLabel = primaryButtonLabel;
        this.primaryButtonUrl = primaryButtonUrl;
        this.secondaryButtonLabel = secondaryButtonLabel;
        this.secondaryButtonUrl = secondaryButtonUrl;
        this.language = language;
        this.status = status;
        this.featured = featured;
        this.displayOrder = displayOrder;

        // Set aliases
        this.description = body;
        this.imageUrl = backgroundImageUrl;
        this.sortOrder = displayOrder;
        this.active = status == ContentStatus.PUBLISHED;
    }
}