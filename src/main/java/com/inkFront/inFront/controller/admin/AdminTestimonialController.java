package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.TestimonialDTO;
import com.inkFront.inFront.service.content.TestimonialService;
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
@RequestMapping("/api/admin/testimonials")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTestimonialController {

    private final TestimonialService testimonialService;

    @GetMapping
    public ResponseEntity<List<TestimonialDTO>> getAll() {

        log.info("Fetching all testimonials");

        return ResponseEntity.ok(
                testimonialService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestimonialDTO> getById(
            @PathVariable @Positive Long id
    ) {

        log.info("Fetching testimonial {}", id);

        return ResponseEntity.ok(
                testimonialService.getById(id)
        );
    }

    @PostMapping
    public ResponseEntity<TestimonialDTO> create(
            @Valid @RequestBody TestimonialDTO dto
    ) {

        log.info("Creating testimonial: {}", dto.getCompanyName());

        TestimonialDTO created = testimonialService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestimonialDTO> update(

            @PathVariable @Positive Long id,

            @Valid
            @RequestBody TestimonialDTO dto

    ) {

        log.info("Updating testimonial {}", id);

        return ResponseEntity.ok(
                testimonialService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting testimonial {}", id);

        testimonialService.delete(id);

        return ResponseEntity.noContent().build();
    }
}