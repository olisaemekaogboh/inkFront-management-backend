package com.inkFront.inFront.dto.newsletter;

import com.inkFront.inFront.dto.common.AuditDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewsletterSubscriptionResponseDTO extends AuditDTO {
    private String email;
    private Boolean active;
    private SupportedLanguage language;
}