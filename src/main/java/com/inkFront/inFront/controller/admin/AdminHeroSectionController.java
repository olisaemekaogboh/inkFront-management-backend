package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.service.content.HeroSectionService;
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
@RequestMapping("/api/admin/hero-sections")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminHeroSectionController {

    private final HeroSectionService heroSectionService;

    @GetMapping
    public ResponseEntity<List<HeroSectionDTO>> getAll() {

        log.info("Fetching all hero sections");

        return ResponseEntity.ok(heroSectionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroSectionDTO> getById(
            @PathVariable @Positive Long id
    ) {

        log.info("Fetching hero section {}", id);

        return ResponseEntity.ok(heroSectionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HeroSectionDTO> create(
            @Valid @RequestBody HeroSectionDTO dto
    ) {

        log.info("Creating hero section: {}", dto.getTitle());

        HeroSectionDTO created = heroSectionService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeroSectionDTO> update(

            @PathVariable @Positive Long id,

            @Valid
            @RequestBody HeroSectionDTO dto

    ) {

        log.info("Updating hero section {}", id);

        HeroSectionDTO updated = heroSectionService.update(id, dto);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting hero section {}", id);

        heroSectionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}