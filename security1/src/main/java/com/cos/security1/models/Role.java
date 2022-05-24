package com.cos.security1.models;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    String role;

    Role(String role){
        this.role=role;
    }
}
