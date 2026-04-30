package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.entity.HomepageSection;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface HomepageSectionMapper {

    HomepageSectionDTO toDto(HomepageSection entity);

    HomepageSection toEntity(HomepageSectionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(HomepageSectionDTO dto, @MappingTarget HomepageSection entity);

    @AfterMapping
    default void fillFrontendAliases(HomepageSection entity, @MappingTarget HomepageSectionDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        dto.setDescription(entity.getBody());
        dto.setSortOrder(entity.getDisplayOrder());
        dto.setActive(entity.getStatus() != null && "PUBLISHED".equals(entity.getStatus().name()));
    }

    @AfterMapping
    default void fillEntityAliases(HomepageSectionDTO dto, @MappingTarget HomepageSection entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (isBlank(entity.getBody()) && !isBlank(dto.getDescription())) {
            entity.setBody(dto.getDescription());
        }

        if (entity.getDisplayOrder() == null && dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}