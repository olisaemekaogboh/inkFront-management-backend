package com.inkFront.inFront.dto.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AuditDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}