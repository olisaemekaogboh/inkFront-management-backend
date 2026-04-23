package com.inkFront.inFront.service.impl;

import com.inkFront.inFront.dto.admin.AdminDashboardMetricDTO;
import com.inkFront.inFront.dto.admin.AdminDashboardOverviewDTO;
import com.inkFront.inFront.service.AdminDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Override
    public AdminDashboardOverviewDTO getOverview() {
        List<AdminDashboardMetricDTO> metrics = List.of(
                new AdminDashboardMetricDTO("admin", "Admin access", 1L)
        );

        return new AdminDashboardOverviewDTO(metrics);
    }
}