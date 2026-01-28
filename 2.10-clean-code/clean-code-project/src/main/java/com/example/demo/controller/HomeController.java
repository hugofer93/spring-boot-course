package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador principal que muestra información sobre el proyecto.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Clean Code Project");
        response.put("description", "Proyecto de ejemplo para demostrar principios Clean Code");
        response.put("endpoints", Map.of(
                "/api/calculator", "Calculadora con ejemplos de Clean Code",
                "/api/user", "Gestión de usuarios con ejemplos de Clean Code"
        ));
        return ResponseEntity.ok(response);
    }
}
