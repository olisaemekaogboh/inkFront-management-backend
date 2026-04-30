package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.SlugDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductBlueprintDTO extends SlugDTO {

    private String title;

    private String summary;
    private String shortDescription;

    private String description;
    private String fullDescription;

    private String challengeStatement;
    private String solutionOverview;
    private String featureHighlights;

    private String heroImageUrl;
    private String imageUrl;

    private SupportedLanguage language;
    private ContentStatus status;

    private Boolean featured;
    private Boolean active;

    private Integer displayOrder;
    private Integer sortOrder;
}