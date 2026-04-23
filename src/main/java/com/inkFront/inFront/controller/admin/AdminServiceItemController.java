package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ServiceDTO;
import com.inkFront.inFront.service.content.ServiceItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
public class AdminServiceItemController {

    private final ServiceItemService serviceItemService;

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAll() {
        return ResponseEntity.ok(serviceItemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceItemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> create(@RequestBody ServiceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceItemService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> update(
            @PathVariable Long id,
            @RequestBody ServiceDTO dto
    ) {
        return ResponseEntity.ok(serviceItemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}