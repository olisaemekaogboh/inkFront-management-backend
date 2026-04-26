package com.inkFront.inFront.dto.content;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettingDTO {

    private Long id;
    private String settingGroup;
    private String settingKey;
    private String settingValue;
    private String valueType;
    private String language;
    private String status;
    private Integer displayOrder;
}