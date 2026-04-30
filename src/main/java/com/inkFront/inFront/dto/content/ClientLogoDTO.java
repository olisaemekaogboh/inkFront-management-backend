package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.AuditDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientLogoDTO extends AuditDTO {

    private String name;
    private String logoUrl;
    private String websiteUrl;

    private SupportedLanguage language;
    private ContentStatus status;

    private Boolean featured;
    private Boolean active;

    private Integer displayOrder;
    private Integer sortOrder;
}