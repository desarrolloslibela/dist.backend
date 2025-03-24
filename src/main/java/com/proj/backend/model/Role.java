package com.proj.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    // 🔹 Constructor vacío (obligatorio para Hibernate)
    public Role() {}

    // 🔹 Constructor que recibe SOLO el RoleType
    public Role(RoleType name) {
        this.name = name;
    }

    // 🔹 Constructor que recibe id y RoleType (opcional)
    public Role(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}
