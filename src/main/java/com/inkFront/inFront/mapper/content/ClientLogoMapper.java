package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.entity.ClientLogo;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClientLogoMapper {

    ClientLogoDTO toDto(ClientLogo entity);

    ClientLogo toEntity(ClientLogoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ClientLogoDTO dto, @MappingTarget ClientLogo entity);

    @AfterMapping
    default void fillFrontendAliases(ClientLogo entity, @MappingTarget ClientLogoDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        dto.setSortOrder(entity.getDisplayOrder());
        dto.setActive(entity.getStatus() != null && "PUBLISHED".equals(entity.getStatus().name()));
    }

    @AfterMapping
    default void fillEntityAliases(ClientLogoDTO dto, @MappingTarget ClientLogo entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (entity.getDisplayOrder() == null && dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        }
    }
}