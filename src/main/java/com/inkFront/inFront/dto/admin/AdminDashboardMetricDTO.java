package com.inkFront.inFront.dto.admin;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardMetricDTO {
    private String key;
    private String label;
    private long value;
}