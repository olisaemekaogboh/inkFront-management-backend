package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.HomepageSectionDTO;
import com.inkFront.inFront.entity.HomepageSection;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HomepageSectionMapper {

    HomepageSectionDTO toDto(HomepageSection entity);

    HomepageSection toEntity(HomepageSectionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(HomepageSectionDTO dto, @MappingTarget HomepageSection entity);
}