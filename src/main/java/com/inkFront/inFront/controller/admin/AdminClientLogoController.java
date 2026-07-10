package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.service.content.ClientLogoService;
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
@RequestMapping("/api/admin/client-logos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminClientLogoController {

    private final ClientLogoService clientLogoService;

    @GetMapping
    public ResponseEntity<List<ClientLogoDTO>> getAll() {

        return ResponseEntity.ok(clientLogoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientLogoDTO> getById(
            @PathVariable @Positive Long id
    ) {

        return ResponseEntity.ok(clientLogoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ClientLogoDTO> create(
            @Valid @RequestBody ClientLogoDTO dto
    ) {

        log.info("Creating client logo");

        ClientLogoDTO logo = clientLogoService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(logo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientLogoDTO> update(

            @PathVariable @Positive Long id,

            @Valid @RequestBody ClientLogoDTO dto

    ) {

        log.info("Updating client logo {}", id);

        return ResponseEntity.ok(
                clientLogoService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting client logo {}", id);

        clientLogoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}