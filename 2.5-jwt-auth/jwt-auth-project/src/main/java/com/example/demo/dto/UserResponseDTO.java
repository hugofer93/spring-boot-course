package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de usuarios.
 * 
 * Nunca exponemos la contraseña en las respuestas HTTP.
 * Este DTO solo incluye los datos seguros que el cliente necesita.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean enabled;
}
