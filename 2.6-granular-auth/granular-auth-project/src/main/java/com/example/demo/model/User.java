package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Usuario.
 * 
 * Representa un usuario en el sistema con su rol asociado.
 * Los roles definen qué acciones puede realizar el usuario.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario.
     * 
     * Roles disponibles:
     * - ROLE_USER: Usuario básico (solo lectura)
     * - ROLE_MODERATOR: Moderador (puede editar contenido)
     * - ROLE_ADMIN: Administrador (acceso completo)
     */
    @Column(nullable = false)
    private String role;

    private boolean enabled = true;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
