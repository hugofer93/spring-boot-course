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
        response.put("message", "Testing Project API");
        response.put("description", "Proyecto de ejemplo para pruebas unitarias e integración");
        response.put("endpoints", Map.of(
                "calculator", "/api/calculator",
                "add", "/api/calculator/add?a=5&b=3",
                "subtract", "/api/calculator/subtract?a=5&b=3",
                "multiply", "/api/calculator/multiply?a=5&b=3",
                "divide", "/api/calculator/divide?a=10&b=2",
                "square", "/api/calculator/square/5"
        ));
        return ResponseEntity.ok(response);
    }
}
