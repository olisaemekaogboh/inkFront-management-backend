package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.service.content.HomepageSectionService;
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
@RequestMapping("/api/admin/homepage-sections")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminHomepageSectionController {

    private final HomepageSectionService homepageSectionService;

    @GetMapping
    public ResponseEntity<List<HomepageSectionDTO>> getAll() {

        log.info("Fetching homepage sections");

        return ResponseEntity.ok(homepageSectionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomepageSectionDTO> getById(
            @PathVariable @Positive Long id
    ) {

        log.info("Fetching homepage section {}", id);

        return ResponseEntity.ok(homepageSectionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HomepageSectionDTO> create(
            @Valid @RequestBody HomepageSectionDTO dto
    ) {

        log.info("Creating homepage section");

        HomepageSectionDTO created = homepageSectionService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HomepageSectionDTO> update(

            @PathVariable @Positive Long id,

            @Valid
            @RequestBody HomepageSectionDTO dto

    ) {

        log.info("Updating homepage section {}", id);

        return ResponseEntity.ok(
                homepageSectionService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting homepage section {}", id);

        homepageSectionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}