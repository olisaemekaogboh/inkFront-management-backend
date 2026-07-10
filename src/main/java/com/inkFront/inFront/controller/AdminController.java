package com.inkFront.inFront.controller;

import com.inkFront.inFront.dto.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> adminHealth() {

        log.info("Admin health endpoint accessed");

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin route is accessible",
                        Map.of(
                                "scope", "ADMIN_ONLY",
                                "status", "OK"
                        )
                )
        );
    }
}