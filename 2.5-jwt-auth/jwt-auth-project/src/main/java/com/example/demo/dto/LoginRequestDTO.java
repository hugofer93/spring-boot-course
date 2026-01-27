package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las peticiones de login.
 * 
 * Contiene las credenciales que el usuario envía para autenticarse.
 * 
 * @Data genera getters, setters, equals, hashCode y toString
 * @NoArgsConstructor es necesario para la deserialización JSON de Jackson
 */
@Data
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
