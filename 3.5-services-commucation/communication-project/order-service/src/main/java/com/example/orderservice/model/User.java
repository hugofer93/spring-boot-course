package com.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de Usuario (DTO para comunicación con user-service)
 * 
 * Este modelo debe coincidir con el modelo del user-service
 * para que Feign pueda deserializar correctamente la respuesta.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String address;
}
