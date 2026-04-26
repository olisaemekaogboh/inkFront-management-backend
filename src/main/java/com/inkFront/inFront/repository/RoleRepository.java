package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.enums.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(SystemRole name);
}