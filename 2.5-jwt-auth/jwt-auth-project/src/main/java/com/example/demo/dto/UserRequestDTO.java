package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para la creación de usuarios.
 * 
 * Separamos la entidad User de la capa de presentación (Controller)
 * siguiendo el principio de Clean Architecture.
 * 
 * @NotBlank: Valida que el campo no sea null, vacío o solo espacios
 * @Email: Valida el formato de email
 * @Size: Valida el tamaño mínimo y máximo del campo
 */
@Data
public class UserRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
