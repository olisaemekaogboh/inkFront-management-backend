package com.inkFront.inFront.dto.admin;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardOverviewDTO {
    private List<AdminDashboardMetricDTO> metrics;
}
