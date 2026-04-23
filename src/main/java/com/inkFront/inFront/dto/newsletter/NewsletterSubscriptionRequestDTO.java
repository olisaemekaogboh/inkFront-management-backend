package com.inkFront.inFront.dto.newsletter;

import com.inkFront.inFront.entity.enums.SupportedLanguage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsletterSubscriptionRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 160, message = "Email must not exceed 160 characters")
    private String email;

    @NotNull(message = "Language is required")
    private SupportedLanguage language;
}
