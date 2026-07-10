package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ServiceDTO;
import com.inkFront.inFront.service.content.ServiceItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceItemController {

    private final ServiceItemService serviceItemService;

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAll() {

        log.info("Fetching all services");

        return ResponseEntity.ok(serviceItemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getById(
            @PathVariable @Positive Long id
    ) {

        log.info("Fetching service {}", id);

        return ResponseEntity.ok(serviceItemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> create(
            @Valid @RequestBody ServiceDTO dto
    ) {

        log.info("Creating service: {}", dto.getTitle());

        ServiceDTO created = serviceItemService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> update(

            @PathVariable @Positive Long id,

            @Valid
            @RequestBody ServiceDTO dto

    ) {

        log.info("Updating service {}", id);

        return ResponseEntity.ok(
                serviceItemService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting service {}", id);

        serviceItemService.delete(id);

        return ResponseEntity.noContent().build();
    }
}