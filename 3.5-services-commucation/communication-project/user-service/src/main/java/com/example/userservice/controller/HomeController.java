package com.example.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para el endpoint raíz.
 * 
 * Proporciona información sobre el servicio y los endpoints disponibles.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "User Service");
        response.put("message", "Microservicio para gestión de usuarios");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("port", 8081);
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /", "Información del servicio (este endpoint)");
        endpoints.put("GET /users", "Obtiene todos los usuarios");
        endpoints.put("GET /users/{id}", "Obtiene un usuario por ID");
        endpoints.put("POST /users", "Crea un nuevo usuario");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> description = new HashMap<>();
        description.put("GET /users", "Retorna una lista de todos los usuarios registrados");
        description.put("GET /users/{id}", "Retorna la información de un usuario específico por su ID");
        description.put("POST /users", "Crea un nuevo usuario. Body: {name, email, address}");
        
        response.put("endpointDescriptions", description);
        
        Map<String, Object> feignClient = new HashMap<>();
        feignClient.put("note", "Este servicio puede ser consumido por otros servicios usando Feign Client");
        feignClient.put("serviceName", "user-service");
        feignClient.put("example", "@FeignClient(name = \"user-service\")");
        
        response.put("feignClient", feignClient);
        
        return ResponseEntity.ok(response);
    }
}
