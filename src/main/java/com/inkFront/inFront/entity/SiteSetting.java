package com.inkFront.inFront.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "site_settings",
        indexes = {
                @Index(name = "idx_site_settings_language_group", columnList = "language, setting_group"),
                @Index(name = "idx_site_settings_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_site_settings_language_group_key",
                        columnNames = {"language", "setting_group", "setting_key"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_group", nullable = false, length = 80)
    private String settingGroup;

    @Column(name = "setting_key", nullable = false, length = 120)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "value_type", nullable = false, length = 40)
    @Builder.Default
    private String valueType = "TEXT";

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String language = "EN";

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PUBLISHED";

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (language == null || language.isBlank()) language = "EN";
        if (status == null || status.isBlank()) status = "PUBLISHED";
        if (valueType == null || valueType.isBlank()) valueType = "TEXT";
        if (displayOrder == null) displayOrder = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}