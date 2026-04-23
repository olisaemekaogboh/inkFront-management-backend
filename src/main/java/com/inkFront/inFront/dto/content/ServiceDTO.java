package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.SlugDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceDTO extends SlugDTO {
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String category;
    private String iconKey;
    private SupportedLanguage language;
    private ContentStatus status;
    private Integer displayOrder;
    private Boolean featured;
}