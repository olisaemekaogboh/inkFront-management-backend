package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.HeroSection;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface HeroSectionMapper {

    HeroSectionDTO toDto(HeroSection entity);

    HeroSection toEntity(HeroSectionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(HeroSectionDTO dto, @MappingTarget HeroSection entity);

    @AfterMapping
    default void fillFrontendAliases(HeroSection entity, @MappingTarget HeroSectionDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        dto.setDescription(entity.getBody());
        dto.setImageUrl(entity.getBackgroundImageUrl());
        dto.setSortOrder(entity.getDisplayOrder());
        dto.setActive(entity.getStatus() != null && "PUBLISHED".equals(entity.getStatus().name()));
    }

    @AfterMapping
    default void fillEntityAliases(HeroSectionDTO dto, @MappingTarget HeroSection entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (isBlank(entity.getBody()) && !isBlank(dto.getDescription())) {
            entity.setBody(dto.getDescription());
        }

        if (isBlank(entity.getBackgroundImageUrl()) && !isBlank(dto.getImageUrl())) {
            entity.setBackgroundImageUrl(dto.getImageUrl());
        }

        if (entity.getDisplayOrder() == null && dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}