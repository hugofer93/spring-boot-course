package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador raíz para información de la API.
 * Este endpoint está oculto en Swagger UI.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    @Hidden
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Productos - Documentación con Swagger/OpenAPI 3");
        response.put("version", "1.0.0");
        response.put("documentation", Map.of(
                "swagger-ui", "/swagger-ui.html",
                "openapi-json", "/v3/api-docs",
                "openapi-yaml", "/v3/api-docs.yaml"
        ));
        response.put("endpoints", Map.of(
                "products", "/api/products"
        ));
        return ResponseEntity.ok(response);
    }
}
