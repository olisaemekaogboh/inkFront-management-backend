package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.TestimonialDTO;
import com.inkFront.inFront.entity.Testimonial;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TestimonialMapper {

    TestimonialDTO toDto(Testimonial entity);

    Testimonial toEntity(TestimonialDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TestimonialDTO dto, @MappingTarget Testimonial entity);

    @AfterMapping
    default void fillFrontendAliases(Testimonial entity, @MappingTarget TestimonialDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        dto.setCompanyName(entity.getOrganization());
        dto.setSortOrder(entity.getDisplayOrder());
        dto.setActive(entity.getStatus() != null && "PUBLISHED".equals(entity.getStatus().name()));
    }

    @AfterMapping
    default void fillEntityAliases(TestimonialDTO dto, @MappingTarget Testimonial entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (isBlank(entity.getOrganization()) && !isBlank(dto.getCompanyName())) {
            entity.setOrganization(dto.getCompanyName());
        }

        if (entity.getDisplayOrder() == null && dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}