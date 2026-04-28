package com.inkFront.inFront.service.impl;

import com.inkFront.inFront.dto.content.BlogMediaDTO;
import com.inkFront.inFront.dto.content.BlogPostDTO;
import com.inkFront.inFront.entity.BlogMedia;
import com.inkFront.inFront.entity.BlogPost;
import com.inkFront.inFront.entity.enums.BlogMediaType;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.mapper.content.BlogPostMapper;
import com.inkFront.inFront.repository.BlogPostRepository;
import com.inkFront.inFront.service.content.BlogPostService;
import com.inkFront.inFront.util.SlugUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostServiceImpl implements BlogPostService {

    private static final SupportedLanguage DEFAULT_LANGUAGE = SupportedLanguage.EN;

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;

    @Override
    public BlogPostDTO create(BlogPostDTO request) {
        BlogPost blogPost = new BlogPost();
        applyRequest(blogPost, request, false);

        if (StringUtils.hasText(request.getSlug())) {
            blogPost.setSlug(generateUniqueSlug(request.getSlug(), null));
        } else {
            blogPost.setSlug(generateUniqueSlug(blogPost.getTitle(), null));
        }

        BlogPost saved = blogPostRepository.save(blogPost);
        return blogPostMapper.toDTO(saved);
    }
    @Override
    public BlogPostDTO update(Long id, BlogPostDTO request) {
        BlogPost blogPost = getPostEntity(id);

        applyRequest(blogPost, request, true);

        if (StringUtils.hasText(request.getSlug())) {
            blogPost.setSlug(generateUniqueSlug(request.getSlug(), id));
        }

        if (blogPost.getTags() != null) {
            blogPost.setTags(new ArrayList<>(blogPost.getTags()));
        } else {
            blogPost.setTags(new ArrayList<>());
        }

        BlogPost saved = blogPostRepository.save(blogPost);
        return blogPostMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        BlogPost blogPost = getPostEntity(id);
        blogPostRepository.delete(blogPost);
    }

    @Override
    public BlogPostDTO publish(Long id) {
        BlogPost blogPost = getPostEntity(id);
        blogPost.setStatus(ContentStatus.PUBLISHED);

        if (blogPost.getPublishedAt() == null) {
            blogPost.setPublishedAt(LocalDateTime.now());
        }

        return blogPostMapper.toDTO(blogPostRepository.save(blogPost));
    }

    @Override
    public BlogPostDTO unpublish(Long id) {
        BlogPost blogPost = getPostEntity(id);
        blogPost.setStatus(ContentStatus.DRAFT);
        return blogPostMapper.toDTO(blogPostRepository.save(blogPost));
    }

    @Override
    public BlogPostDTO archive(Long id) {
        BlogPost blogPost = getPostEntity(id);
        blogPost.setStatus(ContentStatus.ARCHIVED);
        return blogPostMapper.toDTO(blogPostRepository.save(blogPost));
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostDTO findById(Long id) {
        return blogPostMapper.toDTO(getPostEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPostDTO> findAllAdmin(
            SupportedLanguage language,
            ContentStatus status,
            String search,
            int page,
            int size
    ) {
        Pageable pageable = createPageable(page, size);

        Page<BlogPost> posts;

        if (StringUtils.hasText(search)) {
            String keyword = search.trim();

            posts = blogPostRepository
                    .findAllByTitleContainingIgnoreCaseOrExcerptContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(
                            keyword,
                            keyword,
                            keyword,
                            keyword,
                            pageable
                    );
        } else if (language != null && status != null) {
            posts = blogPostRepository.findAllByLanguageAndStatus(language, status, pageable);
        } else if (language != null) {
            posts = blogPostRepository.findAllByLanguage(language, pageable);
        } else if (status != null) {
            posts = blogPostRepository.findAllByStatus(status, pageable);
        } else {
            posts = blogPostRepository.findAll(pageable);
        }

        return posts.map(blogPostMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPostDTO> findPublished(SupportedLanguage language, int page, int size) {
        Pageable pageable = createPageable(page, size);

        Page<BlogPost> posts = language == null
                ? blogPostRepository.findAllByStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                ContentStatus.PUBLISHED,
                pageable
        )
                : blogPostRepository.findAllByLanguageAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                language,
                ContentStatus.PUBLISHED,
                pageable
        );

        if (posts.isEmpty() && language != null && language != DEFAULT_LANGUAGE) {
            posts = blogPostRepository.findAllByLanguageAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                    DEFAULT_LANGUAGE,
                    ContentStatus.PUBLISHED,
                    pageable
            );
        }

        return posts.map(blogPostMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostDTO> findFeatured(SupportedLanguage language) {
        List<BlogPost> posts = language == null
                ? blogPostRepository.findTop6ByStatusAndFeaturedTrueOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                ContentStatus.PUBLISHED
        )
                : blogPostRepository.findTop6ByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                language,
                ContentStatus.PUBLISHED
        );

        if (posts.isEmpty() && language != null && language != DEFAULT_LANGUAGE) {
            posts = blogPostRepository.findTop6ByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                    DEFAULT_LANGUAGE,
                    ContentStatus.PUBLISHED
            );
        }

        return blogPostMapper.toDTOList(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostDTO findPublishedBySlug(String slug, SupportedLanguage language) {
        BlogPost post = blogPostRepository.findWithMediaBySlug(slug)
                .filter(item -> item.getStatus() == ContentStatus.PUBLISHED)
                .orElseThrow(() -> new EntityNotFoundException("Published blog post not found: " + slug));

        if (language == null || post.getLanguage() == language) {
            return blogPostMapper.toDTO(post);
        }

        if (language != DEFAULT_LANGUAGE) {
            BlogPost fallback = blogPostRepository.findWithMediaBySlug(slug)
                    .filter(item -> item.getStatus() == ContentStatus.PUBLISHED)
                    .filter(item -> item.getLanguage() == DEFAULT_LANGUAGE)
                    .orElse(post);

            return blogPostMapper.toDTO(fallback);
        }

        return blogPostMapper.toDTO(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPostDTO> findPublishedByCategory(
            String category,
            SupportedLanguage language,
            int page,
            int size
    ) {
        Pageable pageable = createPageable(page, size);

        Page<BlogPost> posts = language == null
                ? blogPostRepository.findAllByCategoryIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                category,
                ContentStatus.PUBLISHED,
                pageable
        )
                : blogPostRepository.findAllByLanguageAndCategoryIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                language,
                category,
                ContentStatus.PUBLISHED,
                pageable
        );

        if (posts.isEmpty() && language != null && language != DEFAULT_LANGUAGE) {
            posts = blogPostRepository.findAllByLanguageAndCategoryIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                    DEFAULT_LANGUAGE,
                    category,
                    ContentStatus.PUBLISHED,
                    pageable
            );
        }

        return posts.map(blogPostMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPostDTO> findPublishedByTag(
            String tag,
            SupportedLanguage language,
            int page,
            int size
    ) {
        Pageable pageable = createPageable(page, size);

        Page<BlogPost> posts = language == null
                ? blogPostRepository.findDistinctByTagsIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                tag,
                ContentStatus.PUBLISHED,
                pageable
        )
                : blogPostRepository.findDistinctByLanguageAndTagsIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                language,
                tag,
                ContentStatus.PUBLISHED,
                pageable
        );

        if (posts.isEmpty() && language != null && language != DEFAULT_LANGUAGE) {
            posts = blogPostRepository.findDistinctByLanguageAndTagsIgnoreCaseAndStatusOrderByDisplayOrderAscPublishedAtDescCreatedAtDesc(
                    DEFAULT_LANGUAGE,
                    tag,
                    ContentStatus.PUBLISHED,
                    pageable
            );
        }

        return posts.map(blogPostMapper::toDTO);
    }

    private BlogPost getPostEntity(Long id) {
        return blogPostRepository.findWithMediaById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog post not found with id: " + id));
    }

    private Pageable createPageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);

        return PageRequest.of(
                safePage,
                safeSize,
                Sort.by(
                        Sort.Order.asc("displayOrder"),
                        Sort.Order.desc("publishedAt"),
                        Sort.Order.desc("createdAt")
                )
        );
    }

    private void applyRequest(BlogPost blogPost, BlogPostDTO request, boolean updating) {
        if (request == null) {
            throw new IllegalArgumentException("Blog post request is required");
        }

        if (!updating || request.getTitle() != null) {
            blogPost.setTitle(cleanRequired(request.getTitle(), "Title is required"));
        }

        if (!updating || request.getExcerpt() != null) {
            blogPost.setExcerpt(cleanOptional(request.getExcerpt()));
        }

        if (!updating || request.getContent() != null) {
            blogPost.setContent(cleanOptional(request.getContent()));
        }

        if (!updating || request.getFeaturedImageUrl() != null) {
            blogPost.setFeaturedImageUrl(cleanOptional(request.getFeaturedImageUrl()));
        }

        if (!updating || request.getVideoUrl() != null) {
            blogPost.setVideoUrl(cleanOptional(request.getVideoUrl()));
        }

        if (!updating || request.getEmbedVideoUrl() != null) {
            blogPost.setEmbedVideoUrl(cleanOptional(request.getEmbedVideoUrl()));
        }

        if (!updating || request.getAuthorName() != null) {
            blogPost.setAuthorName(cleanOptional(request.getAuthorName()));
        }

        if (!updating || request.getCategory() != null) {
            blogPost.setCategory(cleanOptional(request.getCategory()));
        }

        if (!updating || request.getLanguage() != null) {
            blogPost.setLanguage(request.getLanguage() == null ? DEFAULT_LANGUAGE : request.getLanguage());
        }

        if (!updating || request.getStatus() != null) {
            ContentStatus status = request.getStatus() == null ? ContentStatus.DRAFT : request.getStatus();
            blogPost.setStatus(status);

            if (status == ContentStatus.PUBLISHED && blogPost.getPublishedAt() == null) {
                blogPost.setPublishedAt(LocalDateTime.now());
            }
        }

        if (!updating || request.getFeatured() != null) {
            blogPost.setFeatured(Boolean.TRUE.equals(request.getFeatured()));
        }

        if (!updating || request.getDisplayOrder() != null) {
            blogPost.setDisplayOrder(request.getDisplayOrder() == null ? 0 : request.getDisplayOrder());
        }

        if (!updating || request.getTags() != null) {
            blogPost.setTags(normalizeTags(request.getTags()));
        }

        if (!updating || request.getMedia() != null) {
            replaceMedia(blogPost, request.getMedia());
        }
    }

    private void replaceMedia(BlogPost blogPost, List<BlogMediaDTO> mediaRequests) {
        blogPost.clearMedia();

        if (mediaRequests == null || mediaRequests.isEmpty()) {
            return;
        }

        int index = 0;

        for (BlogMediaDTO mediaRequest : mediaRequests) {
            if (mediaRequest == null || !StringUtils.hasText(mediaRequest.getMediaUrl())) {
                continue;
            }

            BlogMedia media = new BlogMedia();
            media.setMediaType(mediaRequest.getMediaType() == null ? BlogMediaType.IMAGE : mediaRequest.getMediaType());
            media.setMediaUrl(mediaRequest.getMediaUrl().trim());
            media.setDisplayOrder(mediaRequest.getDisplayOrder() == null ? index : mediaRequest.getDisplayOrder());

            blogPost.addMedia(media);
            index++;
        }
    }

    private List<String> normalizeTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }

        return tags.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .map(tag -> tag.replaceAll("\\s+", " "))
                .distinct()
                .toList();
    }

    private String cleanRequired(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private String cleanOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String generateUniqueSlug(String value, Long currentId) {
        String baseSlug = SlugUtil.toSlug(value);

        if (!StringUtils.hasText(baseSlug)) {
            baseSlug = "blog-post";
        }

        String candidate = baseSlug;
        int counter = 2;

        while (slugExists(candidate, currentId)) {
            candidate = baseSlug + "-" + counter;
            counter++;
        }

        return candidate;
    }

    private boolean slugExists(String slug, Long currentId) {
        if (currentId == null) {
            return blogPostRepository.existsBySlug(slug);
        }

        return blogPostRepository.existsBySlugAndIdNot(slug, currentId);
    }
}