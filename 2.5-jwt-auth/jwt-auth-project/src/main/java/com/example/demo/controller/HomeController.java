package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para el endpoint raíz.
 * 
 * Proporciona información sobre la API y los endpoints disponibles.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to the Spring Boot JWT Authentication API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("authentication", "JWT (JSON Web Token)");
        
        Map<String, String> apiEndpoints = new HashMap<>();
        apiEndpoints.put("POST /api/users", "Register a new user (Public - No authentication required)");
        apiEndpoints.put("POST /api/auth/login", "Login and get JWT token (Public - No authentication required)");
        apiEndpoints.put("GET /api/users", "List all users (Protected - Requires JWT token)");
        
        response.put("endpoints", apiEndpoints);
        
        Map<String, String> usage = new HashMap<>();
        usage.put("1", "Register a user with POST /api/users");
        usage.put("2", "Login with POST /api/auth/login to get a JWT token");
        usage.put("3", "Use the token in Authorization header: Bearer <token>");
        usage.put("4", "Access protected endpoints with the token");
        
        response.put("usage", usage);
        
        return ResponseEntity.ok(response);
    }
}
