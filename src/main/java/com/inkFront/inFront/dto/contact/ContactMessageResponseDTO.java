package com.inkFront.inFront.dto.contact;

import com.inkFront.inFront.dto.common.AuditDTO;
import com.inkFront.inFront.entity.enums.LeadStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactMessageResponseDTO extends AuditDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String companyName;
    private String subject;
    private String message;
    private SupportedLanguage preferredLanguage;
    private LeadStatus status;
}
