package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.enums.SystemRole;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private SystemRole name;

    @Column(length = 150)
    private String description;

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public SystemRole getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(SystemRole name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}