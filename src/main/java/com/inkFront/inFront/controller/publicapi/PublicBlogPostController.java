package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.content.BlogPostDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/blog-posts")
@RequiredArgsConstructor
public class PublicBlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<Page<BlogPostDTO>> getPublishedPosts(
            @RequestParam(required = false) SupportedLanguage language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        return ResponseEntity.ok(
                blogPostService.findPublished(language, page, size)
        );
    }

    @GetMapping("/featured")
    public ResponseEntity<List<BlogPostDTO>> getFeaturedPosts(
            @RequestParam(required = false) SupportedLanguage language
    ) {
        return ResponseEntity.ok(blogPostService.findFeatured(language));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BlogPostDTO> getBySlug(
            @PathVariable String slug,
            @RequestParam(required = false) SupportedLanguage language
    ) {
        return ResponseEntity.ok(blogPostService.findPublishedBySlug(slug, language));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<BlogPostDTO>> getByCategory(
            @PathVariable String category,
            @RequestParam(required = false) SupportedLanguage language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        return ResponseEntity.ok(
                blogPostService.findPublishedByCategory(category, language, page, size)
        );
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<Page<BlogPostDTO>> getByTag(
            @PathVariable String tag,
            @RequestParam(required = false) SupportedLanguage language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        return ResponseEntity.ok(
                blogPostService.findPublishedByTag(tag, language, page, size)
        );
    }
}