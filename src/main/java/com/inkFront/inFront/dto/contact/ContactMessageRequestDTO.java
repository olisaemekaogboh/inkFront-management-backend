package com.inkFront.inFront.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 150)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 180)
    private String email;

    @Size(max = 50)
    private String phone;

    @Size(max = 150)
    private String company;

    @Size(max = 150)
    private String serviceInterest;

    @Size(max = 10)
    private String preferredLanguage;

    @NotBlank(message = "Subject is required")
    @Size(max = 200)
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(max = 5000)
    private String message;
}