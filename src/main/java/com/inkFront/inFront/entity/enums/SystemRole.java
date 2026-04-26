package com.inkFront.inFront.entity.enums;

public enum SystemRole {
    USER,
    ADMIN,
    SUPER_ADMIN,

    ROLE_USER,
    ROLE_ADMIN,
    ROLE_SUPER_ADMIN;

    public String normalizedName() {
        return name().startsWith("ROLE_") ? name().substring(5) : name();
    }
}