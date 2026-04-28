package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.entity.enums.BlogMediaType;
import lombok.Data;

@Data
public class BlogMediaRequestDTO {

    private BlogMediaType mediaType;

    private String mediaUrl;

    private Integer displayOrder;
}