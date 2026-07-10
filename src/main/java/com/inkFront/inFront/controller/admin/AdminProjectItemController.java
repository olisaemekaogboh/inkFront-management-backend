package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.service.content.ProjectItemService;
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
@RequestMapping("/api/admin/projects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProjectItemController {

    private final ProjectItemService projectItemService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAll() {

        log.info("Fetching all projects");

        return ResponseEntity.ok(projectItemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(
            @PathVariable @Positive Long id
    ) {

        log.info("Fetching project {}", id);

        return ResponseEntity.ok(projectItemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> create(
            @Valid @RequestBody ProjectDTO dto
    ) {

        log.info("Creating project: {}", dto.getTitle());

        ProjectDTO created = projectItemService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(

            @PathVariable @Positive Long id,

            @Valid
            @RequestBody ProjectDTO dto

    ) {

        log.info("Updating project {}", id);

        return ResponseEntity.ok(
                projectItemService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting project {}", id);

        projectItemService.delete(id);

        return ResponseEntity.noContent().build();
    }
}