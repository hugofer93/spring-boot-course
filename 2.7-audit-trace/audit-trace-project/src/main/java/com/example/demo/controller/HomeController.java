package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador principal.
 * 
 * Endpoint raíz para información de la API.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Auditoría y Trazabilidad");
        response.put("description", "Este proyecto demuestra el uso de filtros e interceptores para auditoría");
        response.put("endpoints", Map.of(
                "GET /", "Información de la API",
                "GET /api/users", "Obtener usuarios (ejemplo de endpoint auditado)",
                "POST /api/users", "Crear usuario (ejemplo de endpoint auditado)",
                "GET /api/audit/logs", "Ver logs de auditoría"
        ));
        return ResponseEntity.ok(response);
    }
}
