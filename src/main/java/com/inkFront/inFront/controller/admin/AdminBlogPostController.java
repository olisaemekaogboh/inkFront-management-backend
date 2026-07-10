package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.BlogPostDTO;
import com.inkFront.inFront.dto.content.BlogPostRequestDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.mapper.content.BlogPostRequestMapper;
import com.inkFront.inFront.service.content.BlogPostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/blog-posts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogPostController {

    private final BlogPostService blogPostService;
    private final BlogPostRequestMapper blogPostRequestMapper;

    @PostMapping
    public ResponseEntity<BlogPostDTO> create(
            @Valid @RequestBody BlogPostRequestDTO request
    ) {

        log.info("Creating blog post: {}", request.getTitle());

        BlogPostDTO blog =
                blogPostService.create(blogPostRequestMapper.toDTO(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(blog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDTO> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody BlogPostRequestDTO request
    ) {

        log.info("Updating blog post {}", id);

        return ResponseEntity.ok(
                blogPostService.update(id, blogPostRequestMapper.toDTO(request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting blog post {}", id);

        blogPostService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<BlogPostDTO> publish(
            @PathVariable @Positive Long id
    ) {

        log.info("Publishing blog post {}", id);

        return ResponseEntity.ok(blogPostService.publish(id));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<BlogPostDTO> unpublish(
            @PathVariable @Positive Long id
    ) {

        log.info("Unpublishing blog post {}", id);

        return ResponseEntity.ok(blogPostService.unpublish(id));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<BlogPostDTO> archive(
            @PathVariable @Positive Long id
    ) {

        log.info("Archiving blog post {}", id);

        return ResponseEntity.ok(blogPostService.archive(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDTO> getById(
            @PathVariable @Positive Long id
    ) {

        return ResponseEntity.ok(blogPostService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BlogPostDTO>> getAll(

            @RequestParam(required = false)
            SupportedLanguage language,

            @RequestParam(required = false)
            ContentStatus status,

            @RequestParam(required = false)
            String search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return ResponseEntity.ok(
                blogPostService.findAllAdmin(
                        language,
                        status,
                        search,
                        safePage,
                        safeSize
                )
        );
    }
}