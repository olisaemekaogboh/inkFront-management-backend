package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.HeroSectionDTO;
import com.inkFront.inFront.entity.HeroSection;
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
}