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
    private String challengeStatement;
    private String solutionOverview;
    private String featureHighlights;
    private String heroImageUrl;
    private SupportedLanguage language;
    private ContentStatus status;
    private Boolean featured;
    private Integer displayOrder;
}