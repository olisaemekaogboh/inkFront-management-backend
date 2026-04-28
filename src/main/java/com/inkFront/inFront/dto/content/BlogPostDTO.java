package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.dto.common.SlugDTO;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BlogPostDTO extends SlugDTO {

    private String title;

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

    private LocalDateTime publishedAt;

    private List<BlogMediaDTO> media = new ArrayList<>();
}