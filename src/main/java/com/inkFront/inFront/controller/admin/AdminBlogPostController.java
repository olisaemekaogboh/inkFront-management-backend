package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.BlogPostDTO;
import com.inkFront.inFront.dto.content.BlogPostRequestDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.mapper.content.BlogPostRequestMapper;
import com.inkFront.inFront.service.content.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/blog-posts")
@RequiredArgsConstructor
public class AdminBlogPostController {

    private final BlogPostService blogPostService;
    private final BlogPostRequestMapper blogPostRequestMapper;

    @PostMapping
    public ResponseEntity<BlogPostDTO> create(@RequestBody BlogPostRequestDTO request) {
        return ResponseEntity.ok(
                blogPostService.create(blogPostRequestMapper.toDTO(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDTO> update(
            @PathVariable Long id,
            @RequestBody BlogPostRequestDTO request
    ) {
        return ResponseEntity.ok(
                blogPostService.update(id, blogPostRequestMapper.toDTO(request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogPostService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<BlogPostDTO> publish(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.publish(id));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<BlogPostDTO> unpublish(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.unpublish(id));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<BlogPostDTO> archive(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.archive(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BlogPostDTO>> getAll(
            @RequestParam(required = false) SupportedLanguage language,
            @RequestParam(required = false) ContentStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return ResponseEntity.ok(
                blogPostService.findAllAdmin(language, status, search, safePage, safeSize)
        );
    }
}