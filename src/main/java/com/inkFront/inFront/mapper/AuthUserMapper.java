package com.inkFront.inFront.mapper;

import com.inkFront.inFront.dto.auth.AuthUserDTO;
import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    AuthUserDTO toDto(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return new LinkedHashSet<>();
        }

        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}