package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.BaseEntity;
import com.inkFront.inFront.entity.enums.BlogMediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "blog_media")
public class BlogMedia extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BlogMediaType mediaType = BlogMediaType.IMAGE;

    @Column(nullable = false, length = 500)
    private String mediaUrl;

    @Column(nullable = false)
    private Integer displayOrder = 0;
}