package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to the Spring Security Example API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("users", "/api/users");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> apiEndpoints = new HashMap<>();
        apiEndpoints.put("GET /api/users", "List all users (Requires Authentication)");
        apiEndpoints.put("POST /api/users", "Register a new user (Public)");
        
        response.put("apiEndpoints", apiEndpoints);
        
        return ResponseEntity.ok(response);
    }
}
