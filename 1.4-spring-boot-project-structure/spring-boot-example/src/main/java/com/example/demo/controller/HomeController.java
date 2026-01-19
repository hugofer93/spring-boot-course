package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para manejar la ruta raíz y proporcionar información sobre la API
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenido a Spring Boot Example API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("users", "/api/users");
        endpoints.put("health", "/actuator/health");
        endpoints.put("info", "/actuator/info");
        endpoints.put("metrics", "/actuator/metrics");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> apiEndpoints = new HashMap<>();
        apiEndpoints.put("GET /api/users", "Obtiene todos los usuarios");
        apiEndpoints.put("GET /api/users/{id}", "Obtiene un usuario por ID");
        apiEndpoints.put("POST /api/users", "Crea un nuevo usuario");
        apiEndpoints.put("PUT /api/users/{id}", "Actualiza un usuario");
        apiEndpoints.put("DELETE /api/users/{id}", "Elimina un usuario");
        
        response.put("apiEndpoints", apiEndpoints);
        
        return ResponseEntity.ok(response);
    }
}
