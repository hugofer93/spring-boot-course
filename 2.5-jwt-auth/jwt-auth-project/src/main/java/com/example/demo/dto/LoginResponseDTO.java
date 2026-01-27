package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de login.
 * 
 * Contiene el token JWT que el cliente usará para autenticarse
 * en las siguientes peticiones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String type = "Bearer"; // Tipo de token (estándar OAuth2)
}
