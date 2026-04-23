package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.service.content.ProjectItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/projects")
@RequiredArgsConstructor
public class AdminProjectItemController {

    private final ProjectItemService projectItemService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAll() {
        return ResponseEntity.ok(projectItemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectItemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectItemService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(
            @PathVariable Long id,
            @RequestBody ProjectDTO dto
    ) {
        return ResponseEntity.ok(projectItemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}