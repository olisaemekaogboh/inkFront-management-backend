package com.inkFront.inFront.dto.contact;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageUpdateDTO {

    private String status;
    private String priority;
    private String adminNote;
    private String assignedTo;
    private Boolean markContacted;
}