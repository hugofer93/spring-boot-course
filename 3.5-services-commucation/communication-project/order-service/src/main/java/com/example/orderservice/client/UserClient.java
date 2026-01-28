package com.example.orderservice.client;

import com.example.orderservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign Client para comunicación con el user-service
 * 
 * @FeignClient: Define un cliente REST declarativo
 * - name: Nombre del servicio en Eureka (debe coincidir con spring.application.name del servicio)
 * - url: URL base del servicio (opcional, si no se usa Eureka)
 * 
 * Esta interfaz define los endpoints que se pueden llamar en el user-service.
 * Feign se encarga de generar la implementación automáticamente.
 */
@FeignClient(name = "user-service")
public interface UserClient {

    /**
     * Obtiene todos los usuarios
     * Equivale a: GET http://user-service/users
     */
    @GetMapping("/users")
    List<User> getAllUsers();

    /**
     * Obtiene un usuario por ID
     * Equivale a: GET http://user-service/users/{id}
     */
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);
}
