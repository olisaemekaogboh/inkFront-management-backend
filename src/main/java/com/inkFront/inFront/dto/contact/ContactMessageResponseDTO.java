package com.inkFront.inFront.dto.contact;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String company;
    private String serviceInterest;
    private String preferredLanguage;
    private String subject;
    private String message;
    private String status;
    private String priority;
    private String adminNote;
    private String assignedTo;
    private String source;
    private LocalDateTime lastContactedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}