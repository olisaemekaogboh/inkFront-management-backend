package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.BlogPost;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @EntityGraph(attributePaths = {"media"})
    Optional<BlogPost> findWithMediaById(Long id);

    @EntityGraph(attributePaths = {"media"})
    Optional<BlogPost> findWithMediaBySlug(String slug);

    @Override
    Page<BlogPost> findAll(Pageable pageable);

    Page<BlogPost> findAllByLanguage(
            SupportedLanguage language,
            Pageable pageable
    );

    Page<BlogPost> findAllByStatus(
            ContentStatus status,
            Pageable pageable
    );

    Page<BlogPost> findAllByLanguageAndStatus(
            SupportedLanguage language,
            ContentStatus status,
            Pageable pageable
    );

    Page<BlogPost> findAllByStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            ContentStatus status,
            Pageable pageable
    );

    Page<BlogPost> findAllByLanguageAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            SupportedLanguage language,
            ContentStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"media"})
    List<BlogPost> findTop6ByStatusAndFeaturedTrueOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            ContentStatus status
    );

    @EntityGraph(attributePaths = {"media"})
    List<BlogPost> findTop6ByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            SupportedLanguage language,
            ContentStatus status
    );

    Page<BlogPost> findAllByCategoryIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            String category,
            ContentStatus status,
            Pageable pageable
    );

    Page<BlogPost> findAllByLanguageAndCategoryIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            SupportedLanguage language,
            String category,
            ContentStatus status,
            Pageable pageable
    );

    Page<BlogPost> findDistinctByTagsIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            String tag,
            ContentStatus status,
            Pageable pageable
    );

    Page<BlogPost> findDistinctByLanguageAndTagsIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
            SupportedLanguage language,
            String tag,
            ContentStatus status,
            Pageable pageable
    );

    Page<BlogPost> findAllByTitleContainingIgnoreCaseOrExcerptContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(
            String title,
            String excerpt,
            String category,
            String authorName,
            Pageable pageable
    );
}