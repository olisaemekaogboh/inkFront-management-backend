package com.inkFront.inFront.controller;



import com.inkFront.inFront.dto.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> adminHealth() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin route is accessible",
                        Map.of("scope", "ADMIN_ONLY", "status", "OK")
                )
        );
    }
}
