package com.inkFront.inFront.dto.content;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqDTO {

    private Long id;
    private String pageKey;
    private String question;
    private String answer;
    private String language;
    private String status;
    private Integer displayOrder;
}