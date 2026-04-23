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
    private String description;
    private String clientIndustry;
    private String projectType;
    private String coverImageUrl;
    private String liveUrl;
    private SupportedLanguage language;
    private ContentStatus status;
    private Boolean featured;
    private Integer displayOrder;
}
