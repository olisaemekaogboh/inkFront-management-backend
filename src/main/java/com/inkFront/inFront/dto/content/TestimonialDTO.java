package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.AuditDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestimonialDTO extends AuditDTO {

    private String clientName;
    private String clientRole;

    private String organization;
    private String companyName;

    private String quote;
    private String avatarUrl;

    private SupportedLanguage language;
    private ContentStatus status;

    private Boolean featured;
    private Boolean active;

    private Integer displayOrder;
    private Integer sortOrder;
}