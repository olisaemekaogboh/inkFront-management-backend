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
    private String preferredLanguage;
    private String company;
    private String serviceInterest;
    private String subject;
    private String message;
    private String status;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}