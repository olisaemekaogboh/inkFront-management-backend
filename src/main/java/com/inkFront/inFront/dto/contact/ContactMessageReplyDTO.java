package com.inkFront.inFront.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageReplyDTO {

    @Email(message = "Reply-to email must be valid")
    @Size(max = 180, message = "Reply-to email cannot exceed 180 characters")
    private String replyTo;

    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject cannot exceed 200 characters")
    private String subject;

    @NotBlank(message = "Reply message is required")
    @Size(max = 10000, message = "Reply message cannot exceed 10000 characters")
    private String message;

    @Size(max = 5000, message = "Admin note cannot exceed 5000 characters")
    private String adminNote;

    @Size(max = 150, message = "Assigned staff cannot exceed 150 characters")
    private String assignedTo;
}