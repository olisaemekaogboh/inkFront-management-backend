package com.inkFront.inFront.dto.contact;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageStatsDTO {

    private long total;
    private long newMessages;
    private long inProgress;
    private long contacted;
    private long resolved;
    private long archived;
}