package com.inkFront.inFront.controller.admin;



import com.inkFront.inFront.dto.admin.AdminDashboardOverviewDTO;
import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<AdminDashboardOverviewDTO>> getOverview() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin dashboard overview loaded successfully",
                        adminDashboardService.getOverview()
                )
        );
    }
}