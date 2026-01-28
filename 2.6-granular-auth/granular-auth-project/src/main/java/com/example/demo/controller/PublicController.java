package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para endpoints públicos.
 * 
 * Estos endpoints no requieren autenticación.
 * Cualquiera puede acceder a ellos.
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getPublicInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Esta es información pública");
        response.put("access", "No requiere autenticación");
        return ResponseEntity.ok(response);
    }
}
