package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.service.content.HeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/hero-sections")
@RequiredArgsConstructor
public class AdminHeroSectionController {

    private final HeroSectionService heroSectionService;

    @GetMapping
    public ResponseEntity<List<HeroSectionDTO>> getAll() {
        return ResponseEntity.ok(heroSectionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroSectionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(heroSectionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HeroSectionDTO> create(@RequestBody HeroSectionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(heroSectionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeroSectionDTO> update(
            @PathVariable Long id,
            @RequestBody HeroSectionDTO dto
    ) {
        return ResponseEntity.ok(heroSectionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        heroSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}