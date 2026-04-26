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
    private String title;

    private String shortDescription;
    private String summary;

    private String fullDescription;
    private String description;

    private String category;

    private String iconKey;
    private String icon;

    private String imageUrl;

    private SupportedLanguage language;
    private ContentStatus status;

    private Integer displayOrder;
    private Integer sortOrder;

    private Boolean featured;
    private Boolean active;
}