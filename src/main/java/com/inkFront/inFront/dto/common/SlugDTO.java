package com.inkFront.inFront.dto.common;

import lombok.Data;

@Data
public abstract class SlugDTO extends AuditDTO {
    private String slug;
}