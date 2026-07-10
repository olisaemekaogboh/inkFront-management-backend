package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.admin.AdminDashboardOverviewDTO;
import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<AdminDashboardOverviewDTO>> getOverview() {

        log.info("Loading admin dashboard overview");

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin dashboard overview loaded successfully",
                        adminDashboardService.getOverview()
                )
        );
    }
}