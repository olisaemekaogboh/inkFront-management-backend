package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.ProductBlueprintDTO;
import com.inkFront.inFront.entity.ProductBlueprint;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductBlueprintMapper {

    ProductBlueprintDTO toDto(ProductBlueprint entity);

    ProductBlueprint toEntity(ProductBlueprintDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductBlueprintDTO dto, @MappingTarget ProductBlueprint entity);

    @AfterMapping
    default void mapExtras(ProductBlueprint e, @MappingTarget ProductBlueprintDTO d) {
        d.setSortOrder(e.getDisplayOrder());
        d.setActive(e.getStatus() == com.inkFront.inFront.entity.enums.ContentStatus.PUBLISHED);
    }
}