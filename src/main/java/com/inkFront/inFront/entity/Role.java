package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.AuditableEntity;
import com.inkFront.inFront.entity.enums.SystemRole;
import jakarta.persistence.*;

@Entity
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_roles_name", columnNames = "name")
        }
)
public class Role extends AuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, length = 60)
    private SystemRole name;

    public Role() {
    }

    public Role(SystemRole name) {
        this.name = name;
    }

    public SystemRole getName() {
        return name;
    }

    public void setName(SystemRole name) {
        this.name = name;
    }
}