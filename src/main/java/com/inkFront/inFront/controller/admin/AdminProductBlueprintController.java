package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ProductBlueprintDTO;
import com.inkFront.inFront.service.content.ProductBlueprintService;
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
@RequestMapping("/api/admin/product-blueprints")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductBlueprintController {

    private final ProductBlueprintService productBlueprintService;

    @GetMapping
    public ResponseEntity<List<ProductBlueprintDTO>> getAll() {

        log.info("Fetching product blueprints");

        return ResponseEntity.ok(
                productBlueprintService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBlueprintDTO> getById(
            @PathVariable @Positive Long id
    ) {

        log.info("Fetching product blueprint {}", id);

        return ResponseEntity.ok(
                productBlueprintService.getById(id)
        );
    }

    @PostMapping
    public ResponseEntity<ProductBlueprintDTO> create(
            @Valid @RequestBody ProductBlueprintDTO dto
    ) {

        log.info("Creating product blueprint");

        ProductBlueprintDTO created =
                productBlueprintService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBlueprintDTO> update(

            @PathVariable @Positive Long id,

            @Valid
            @RequestBody ProductBlueprintDTO dto

    ) {

        log.info("Updating product blueprint {}", id);

        return ResponseEntity.ok(
                productBlueprintService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting product blueprint {}", id);

        productBlueprintService.delete(id);

        return ResponseEntity.noContent().build();
    }
}