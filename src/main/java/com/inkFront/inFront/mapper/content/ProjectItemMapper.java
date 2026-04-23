package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.entity.ProjectItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectItemMapper {

    ProjectDTO toDto(ProjectItem entity);

    ProjectItem toEntity(ProjectDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProjectDTO dto, @MappingTarget ProjectItem entity);
}