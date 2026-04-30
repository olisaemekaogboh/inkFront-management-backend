package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.entity.ProjectItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProjectItemMapper {

    ProjectDTO toDto(ProjectItem entity);

    ProjectItem toEntity(ProjectDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProjectDTO dto, @MappingTarget ProjectItem entity);

    @AfterMapping
    default void fillFrontendAliases(ProjectItem entity, @MappingTarget ProjectDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        dto.setShortDescription(entity.getSummary());
        dto.setFullDescription(entity.getDescription());

        if (dto.getImageUrl() == null || dto.getImageUrl().isBlank()) {
            dto.setImageUrl(first(entity.getImageUrl(), entity.getCoverImageUrl()));
        }

        if (dto.getCoverImageUrl() == null || dto.getCoverImageUrl().isBlank()) {
            dto.setCoverImageUrl(first(entity.getCoverImageUrl(), entity.getImageUrl()));
        }

        if (dto.getProjectUrl() == null || dto.getProjectUrl().isBlank()) {
            dto.setProjectUrl(first(entity.getProjectUrl(), entity.getLiveUrl()));
        }

        if (dto.getLiveUrl() == null || dto.getLiveUrl().isBlank()) {
            dto.setLiveUrl(first(entity.getLiveUrl(), entity.getProjectUrl()));
        }

        dto.setSortOrder(entity.getDisplayOrder());
        dto.setActive(entity.getStatus() != null && "PUBLISHED".equals(entity.getStatus().name()));
    }

    @AfterMapping
    default void fillEntityAliases(ProjectDTO dto, @MappingTarget ProjectItem entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (isBlank(entity.getSummary()) && !isBlank(dto.getShortDescription())) {
            entity.setSummary(dto.getShortDescription());
        }

        if (isBlank(entity.getDescription()) && !isBlank(dto.getFullDescription())) {
            entity.setDescription(dto.getFullDescription());
        }

        if (isBlank(entity.getCoverImageUrl()) && !isBlank(dto.getImageUrl())) {
            entity.setCoverImageUrl(dto.getImageUrl());
        }

        if (isBlank(entity.getImageUrl()) && !isBlank(dto.getCoverImageUrl())) {
            entity.setImageUrl(dto.getCoverImageUrl());
        }

        if (isBlank(entity.getLiveUrl()) && !isBlank(dto.getProjectUrl())) {
            entity.setLiveUrl(dto.getProjectUrl());
        }

        if (isBlank(entity.getProjectUrl()) && !isBlank(dto.getLiveUrl())) {
            entity.setProjectUrl(dto.getLiveUrl());
        }

        if (entity.getDisplayOrder() == null && dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        }
    }

    private static String first(String... values) {
        if (values == null) return null;

        for (String value : values) {
            if (!isBlank(value)) {
                return value.trim();
            }
        }

        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}