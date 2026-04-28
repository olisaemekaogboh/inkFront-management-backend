package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.BlogPostDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogPostService {

    BlogPostDTO create(BlogPostDTO request);

    BlogPostDTO update(Long id, BlogPostDTO request);

    void delete(Long id);

    BlogPostDTO publish(Long id);

    BlogPostDTO unpublish(Long id);

    BlogPostDTO archive(Long id);

    BlogPostDTO findById(Long id);

    Page<BlogPostDTO> findAllAdmin(
            SupportedLanguage language,
            ContentStatus status,
            String search,
            int page,
            int size
    );

    Page<BlogPostDTO> findPublished(
            SupportedLanguage language,
            int page,
            int size
    );

    List<BlogPostDTO> findFeatured(SupportedLanguage language);

    BlogPostDTO findPublishedBySlug(String slug, SupportedLanguage language);

    Page<BlogPostDTO> findPublishedByCategory(
            String category,
            SupportedLanguage language,
            int page,
            int size
    );

    Page<BlogPostDTO> findPublishedByTag(
            String tag,
            SupportedLanguage language,
            int page,
            int size
    );
}