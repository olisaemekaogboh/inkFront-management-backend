package com.inkFront.inFront.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "contact_messages",
        indexes = {
                @Index(name = "idx_contact_messages_status", columnList = "status"),
                @Index(name = "idx_contact_messages_priority", columnList = "priority"),
                @Index(name = "idx_contact_messages_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 150)
    private String company;

    @Column(name = "service_interest", length = 150)
    private String serviceInterest;

    @Column(name = "preferred_language", nullable = false, length = 10)
    @Builder.Default
    private String preferredLanguage = "EN";

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "NEW";

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String priority = "NORMAL";

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @Column(name = "assigned_to", length = 150)
    private String assignedTo;

    @Column(name = "source", length = 80)
    @Builder.Default
    private String source = "WEBSITE";

    @Column(name = "last_contacted_at")
    private LocalDateTime lastContactedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
        if (status == null || status.isBlank()) status = "NEW";
        if (priority == null || priority.isBlank()) priority = "NORMAL";
        if (preferredLanguage == null || preferredLanguage.isBlank()) preferredLanguage = "EN";
        if (source == null || source.isBlank()) source = "WEBSITE";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        if (status == null || status.isBlank()) status = "NEW";
        if (priority == null || priority.isBlank()) priority = "NORMAL";
        if (preferredLanguage == null || preferredLanguage.isBlank()) preferredLanguage = "EN";

        if ("RESOLVED".equalsIgnoreCase(status) && resolvedAt == null) {
            resolvedAt = LocalDateTime.now();
        }
    }
}