package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.BlogMediaDTO;
import com.inkFront.inFront.dto.content.BlogPostDTO;
import com.inkFront.inFront.entity.BlogMedia;
import com.inkFront.inFront.entity.BlogPost;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlogPostMapper {

    public BlogPostDTO toDTO(BlogPost blogPost) {
        if (blogPost == null) {
            return null;
        }

        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(blogPost.getId());
        dto.setSlug(blogPost.getSlug());
        dto.setTitle(blogPost.getTitle());
        dto.setExcerpt(blogPost.getExcerpt());
        dto.setContent(blogPost.getContent());
        dto.setFeaturedImageUrl(blogPost.getFeaturedImageUrl());
        dto.setVideoUrl(blogPost.getVideoUrl());
        dto.setEmbedVideoUrl(blogPost.getEmbedVideoUrl());
        dto.setAuthorName(blogPost.getAuthorName());
        dto.setCategory(blogPost.getCategory());
        dto.setTags(blogPost.getTags() == null ? new ArrayList<>() : new ArrayList<>(blogPost.getTags()));
        dto.setLanguage(blogPost.getLanguage());
        dto.setStatus(blogPost.getStatus());
        dto.setFeatured(blogPost.getFeatured());
        dto.setDisplayOrder(blogPost.getDisplayOrder());
        dto.setPublishedAt(blogPost.getPublishedAt());
        dto.setCreatedAt(blogPost.getCreatedAt());
        dto.setUpdatedAt(blogPost.getUpdatedAt());
        dto.setMedia(toMediaDTOList(blogPost.getMedia()));

        return dto;
    }

    public List<BlogPostDTO> toDTOList(List<BlogPost> blogPosts) {
        if (blogPosts == null) {
            return new ArrayList<>();
        }

        return blogPosts.stream()
                .map(this::toDTO)
                .toList();
    }

    public BlogMediaDTO toMediaDTO(BlogMedia blogMedia) {
        if (blogMedia == null) {
            return null;
        }

        BlogMediaDTO dto = new BlogMediaDTO();
        dto.setId(blogMedia.getId());
        dto.setMediaType(blogMedia.getMediaType());
        dto.setMediaUrl(blogMedia.getMediaUrl());
        dto.setDisplayOrder(blogMedia.getDisplayOrder());

        return dto;
    }

    public List<BlogMediaDTO> toMediaDTOList(List<BlogMedia> media) {
        if (media == null) {
            return new ArrayList<>();
        }

        return media.stream()
                .map(this::toMediaDTO)
                .toList();
    }
}