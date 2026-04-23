package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ProductBlueprintDTO;
import com.inkFront.inFront.service.content.ProductBlueprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product-blueprints")
@RequiredArgsConstructor
public class AdminProductBlueprintController {

    private final ProductBlueprintService productBlueprintService;

    @GetMapping
    public ResponseEntity<List<ProductBlueprintDTO>> getAll() {
        return ResponseEntity.ok(productBlueprintService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBlueprintDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productBlueprintService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductBlueprintDTO> create(@RequestBody ProductBlueprintDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productBlueprintService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBlueprintDTO> update(
            @PathVariable Long id,
            @RequestBody ProductBlueprintDTO dto
    ) {
        return ResponseEntity.ok(productBlueprintService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productBlueprintService.delete(id);
        return ResponseEntity.noContent().build();
    }
}