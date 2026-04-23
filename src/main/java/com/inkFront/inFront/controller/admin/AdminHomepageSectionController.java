package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.service.content.HomepageSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/homepage-sections")
@RequiredArgsConstructor
public class AdminHomepageSectionController {

    private final HomepageSectionService homepageSectionService;

    @GetMapping
    public ResponseEntity<List<HomepageSectionDTO>> getAll() {
        return ResponseEntity.ok(homepageSectionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomepageSectionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(homepageSectionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HomepageSectionDTO> create(@RequestBody HomepageSectionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(homepageSectionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HomepageSectionDTO> update(
            @PathVariable Long id,
            @RequestBody HomepageSectionDTO dto
    ) {
        return ResponseEntity.ok(homepageSectionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        homepageSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}