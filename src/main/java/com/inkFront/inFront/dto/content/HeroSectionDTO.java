package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.AuditDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HeroSectionDTO extends AuditDTO {
    private String title;
    private String subtitle;
    private String body;
    private String backgroundImageUrl;
    private String placement;
    private String primaryButtonLabel;
    private String primaryButtonUrl;
    private String secondaryButtonLabel;
    private String secondaryButtonUrl;
    private SupportedLanguage language;
    private ContentStatus status;
    private Boolean featured;
    private Integer displayOrder;
}