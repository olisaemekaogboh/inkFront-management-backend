package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.BlogMediaDTO;
import com.inkFront.inFront.dto.content.BlogMediaRequestDTO;
import com.inkFront.inFront.dto.content.BlogPostDTO;
import com.inkFront.inFront.dto.content.BlogPostRequestDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlogPostRequestMapper {

    public BlogPostDTO toDTO(BlogPostRequestDTO request) {
        if (request == null) {
            return null;
        }

        BlogPostDTO dto = new BlogPostDTO();
        dto.setTitle(request.getTitle());
        dto.setSlug(request.getSlug());
        dto.setExcerpt(request.getExcerpt());
        dto.setContent(request.getContent());
        dto.setFeaturedImageUrl(request.getFeaturedImageUrl());
        dto.setVideoUrl(request.getVideoUrl());
        dto.setEmbedVideoUrl(request.getEmbedVideoUrl());
        dto.setAuthorName(request.getAuthorName());
        dto.setCategory(request.getCategory());
        dto.setTags(request.getTags() == null ? new ArrayList<>() : new ArrayList<>(request.getTags()));
        dto.setLanguage(request.getLanguage());
        dto.setStatus(request.getStatus());
        dto.setFeatured(request.getFeatured());
        dto.setDisplayOrder(request.getDisplayOrder());
        dto.setMedia(toMediaDTOList(request.getMedia()));

        return dto;
    }

    private List<BlogMediaDTO> toMediaDTOList(List<BlogMediaRequestDTO> mediaRequests) {
        if (mediaRequests == null) {
            return new ArrayList<>();
        }

        return mediaRequests.stream()
                .map(this::toMediaDTO)
                .toList();
    }

    private BlogMediaDTO toMediaDTO(BlogMediaRequestDTO request) {
        if (request == null) {
            return null;
        }

        BlogMediaDTO dto = new BlogMediaDTO();
        dto.setMediaType(request.getMediaType());
        dto.setMediaUrl(request.getMediaUrl());
        dto.setDisplayOrder(request.getDisplayOrder());

        return dto;
    }
}