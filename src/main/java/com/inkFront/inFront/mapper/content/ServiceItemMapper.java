package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.ServiceDTO;
import com.inkFront.inFront.entity.ServiceItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {

    ServiceDTO toDto(ServiceItem entity);

    ServiceItem toEntity(ServiceDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ServiceDTO dto, @MappingTarget ServiceItem entity);

    @AfterMapping
    default void fillFrontendAliases(ServiceItem entity, @MappingTarget ServiceDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        dto.setTitle(entity.getName());
        dto.setSummary(entity.getShortDescription());
        dto.setDescription(entity.getFullDescription());
        dto.setIcon(entity.getIconKey());
        dto.setSortOrder(entity.getDisplayOrder());
        dto.setActive(entity.getStatus() != null && "PUBLISHED".equals(entity.getStatus().name()));
    }

    @AfterMapping
    default void fillEntityAliases(ServiceDTO dto, @MappingTarget ServiceItem entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (isBlank(entity.getName()) && !isBlank(dto.getTitle())) {
            entity.setName(dto.getTitle());
        }

        if (isBlank(entity.getShortDescription()) && !isBlank(dto.getSummary())) {
            entity.setShortDescription(dto.getSummary());
        }

        if (isBlank(entity.getFullDescription()) && !isBlank(dto.getDescription())) {
            entity.setFullDescription(dto.getDescription());
        }

        if (isBlank(entity.getIconKey()) && !isBlank(dto.getIcon())) {
            entity.setIconKey(dto.getIcon());
        }

        if (entity.getDisplayOrder() == null && dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}