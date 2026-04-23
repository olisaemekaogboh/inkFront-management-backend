package com.inkFront.inFront.mapper.content;

import com.inkFront.inFront.dto.content.TestimonialDTO;
import com.inkFront.inFront.entity.Testimonial;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TestimonialMapper {

    TestimonialDTO toDto(Testimonial entity);

    Testimonial toEntity(TestimonialDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TestimonialDTO dto, @MappingTarget Testimonial entity);
}