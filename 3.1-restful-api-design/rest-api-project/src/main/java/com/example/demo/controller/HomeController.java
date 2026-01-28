package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador raíz para información de la API.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API RESTful Avanzada - Ejemplo de Versionamiento, DTOs y Mappers");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
                "v1", "/api/v1/products",
                "v2", "/api/v2/products"
        ));
        return ResponseEntity.ok(response);
    }
}
