package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.service.content.ClientLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/client-logos")
@RequiredArgsConstructor
public class AdminClientLogoController {

    private final ClientLogoService clientLogoService;

    @GetMapping
    public ResponseEntity<List<ClientLogoDTO>> getAll() {
        return ResponseEntity.ok(clientLogoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientLogoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clientLogoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ClientLogoDTO> create(@RequestBody ClientLogoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientLogoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientLogoDTO> update(
            @PathVariable Long id,
            @RequestBody ClientLogoDTO dto
    ) {
        return ResponseEntity.ok(clientLogoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientLogoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}