package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDTO {
    private SupportedLanguage code;
    private String label;
}