package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlogPostRequestDTO {

    private String title;

    private String slug;

    private String excerpt;

    private String content;

    private String featuredImageUrl;

    private String videoUrl;

    private String embedVideoUrl;

    private String authorName;

    private String category;

    private List<String> tags = new ArrayList<>();

    private SupportedLanguage language;

    private ContentStatus status;

    private Boolean featured;

    private Integer displayOrder;

    private List<BlogMediaRequestDTO> media = new ArrayList<>();
}