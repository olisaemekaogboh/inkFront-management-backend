package com.inkFront.inFront.dto.contact;

import com.inkFront.inFront.entity.enums.SupportedLanguage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactMessageRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name must not exceed 120 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 160, message = "Email must not exceed 160 characters")
    private String email;

    @Size(max = 40, message = "Phone number must not exceed 40 characters")
    private String phoneNumber;

    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String companyName;

    @Size(max = 180, message = "Subject must not exceed 180 characters")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(max = 5000, message = "Message must not exceed 5000 characters")
    private String message;

    @NotNull(message = "Preferred language is required")
    private SupportedLanguage preferredLanguage;
}