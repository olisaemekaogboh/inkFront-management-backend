package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.entity.ClientLogo;
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
}