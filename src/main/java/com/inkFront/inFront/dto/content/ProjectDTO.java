package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.SlugDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectDTO extends SlugDTO {

    private String title;

    private String summary;
    private String shortDescription;

    private String description;
    private String fullDescription;

    private String clientIndustry;
    private String projectType;

    private String clientName;

    private String coverImageUrl;
    private String imageUrl;

    private String liveUrl;
    private String projectUrl;

    private SupportedLanguage language;
    private ContentStatus status;

    private Boolean featured;
    private Boolean active;

    private Integer displayOrder;
    private Integer sortOrder;
}