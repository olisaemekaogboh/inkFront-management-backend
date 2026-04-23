package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.TestimonialDTO;
import com.inkFront.inFront.service.content.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/testimonials")
@RequiredArgsConstructor
public class AdminTestimonialController {

    private final TestimonialService testimonialService;

    @GetMapping
    public ResponseEntity<List<TestimonialDTO>> getAll() {
        return ResponseEntity.ok(testimonialService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestimonialDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(testimonialService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TestimonialDTO> create(@RequestBody TestimonialDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testimonialService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestimonialDTO> update(
            @PathVariable Long id,
            @RequestBody TestimonialDTO dto
    ) {
        return ResponseEntity.ok(testimonialService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        testimonialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}