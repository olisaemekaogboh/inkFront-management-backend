package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.HeroSection;
import com.inkFront.inFront.service.content.HeroSectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admin/hero-sections")
@RequiredArgsConstructor
public class AdminHeroSectionController {

    private final HeroSectionService heroSectionService;

    @GetMapping
    public ResponseEntity<List<HeroSectionDTO>> getAll() {
        log.info("GET all hero sections");
        return ResponseEntity.ok(heroSectionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroSectionDTO> getById(@PathVariable Long id) {
        log.info("GET hero section by id: {}", id);
        return ResponseEntity.ok(heroSectionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HeroSectionDTO> create(@RequestBody HeroSectionDTO dto) {
        log.info("POST create hero section: {}", dto.getTitle());
        log.info("Request body - status: {}, active: {}, featured: {}", dto.getStatus(), dto.getActive(), dto.getFeatured());
        return ResponseEntity.status(HttpStatus.CREATED).body(heroSectionService.create(dto));
    }

    @GetMapping("/debug/check-status")
    public ResponseEntity<?> debugCheckStatus() {
        log.info("Debug: Checking all hero sections status");
        List<HeroSection> allSections = heroSectionService.getAllEntities();

        Map<String, Object> debug = new HashMap<>();
        debug.put("totalCount", allSections.size());
        debug.put("message", "Check the status, language, and placement of each section");

        List<Map<String, Object>> sections = allSections.stream().map(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("title", s.getTitle());
            map.put("status", s.getStatus() != null ? s.getStatus().toString() : "null");
            map.put("language", s.getLanguage() != null ? s.getLanguage().toString() : "null");
            map.put("placement", s.getPlacement());
            map.put("featured", s.getFeatured());
            return map;
        }).collect(Collectors.toList());

        debug.put("sections", sections);

        // Add summary
        long publishedCount = sections.stream()
                .filter(s -> "PUBLISHED".equals(s.get("status")))
                .count();
        debug.put("publishedCount", publishedCount);

        log.info("Debug: Total sections: {}, Published: {}", allSections.size(), publishedCount);

        return ResponseEntity.ok(debug);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeroSectionDTO> update(
            @PathVariable Long id,
            @RequestBody HeroSectionDTO dto
    ) {
        log.info("PUT update hero section id: {}", id);
        log.info("Update payload - status: {}, active: {}, title: {}", dto.getStatus(), dto.getActive(), dto.getTitle());
        HeroSectionDTO updated = heroSectionService.update(id, dto);
        log.info("Update completed - new status: {}", updated.getStatus());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE hero section id: {}", id);
        heroSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}