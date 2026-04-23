package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.ServiceDTO;
import com.inkFront.inFront.entity.ServiceItem;
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
}