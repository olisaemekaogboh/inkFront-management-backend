package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.AuditDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomepageSectionDTO extends AuditDTO {

    private String sectionKey;
    private String title;
    private String subtitle;

    private String body;
    private String description;

    private String imageUrl;

    private SupportedLanguage language;
    private ContentStatus status;

    private Boolean featured;
    private Boolean active;

    private Integer displayOrder;
    private Integer sortOrder;
}