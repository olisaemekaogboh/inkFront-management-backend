package com.inkFront.inFront.entity.base;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SluggableEntity extends AuditableEntity {

    @Column(nullable = false, unique = true, length = 180)
    private String slug;
}