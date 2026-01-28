package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para moderadores.
 * 
 * Estos endpoints requieren rol MODERATOR o superior (ADMIN).
 * Demuestra autorización granular con diferentes métodos HTTP.
 */
@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {

    /**
     * Endpoint GET para moderadores.
     * 
     * La autorización se define en SecurityConfig con hasRole("MODERATOR").
     * También se puede usar @PreAuthorize para mayor granularidad.
     */
    @GetMapping("/content")
    public ResponseEntity<Map<String, Object>> getContent(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Contenido que solo moderadores pueden ver");
        response.put("username", authentication.getName());
        response.put("access", "Requiere rol MODERATOR o ADMIN");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint POST para crear contenido.
     * 
     * Usa @PreAuthorize para autorización granular adicional.
     * hasAnyRole permite especificar múltiples roles.
     */
    @PostMapping("/content")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> createContent(
            @RequestBody Map<String, String> content,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Contenido creado exitosamente");
        response.put("content", content);
        response.put("createdBy", authentication.getName());
        response.put("access", "Requiere rol MODERATOR o ADMIN");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint PUT para editar contenido.
     * 
     * Demuestra autorización granular por método HTTP.
     */
    @PutMapping("/content/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateContent(
            @PathVariable Long id,
            @RequestBody Map<String, String> content,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Contenido actualizado");
        response.put("id", id);
        response.put("content", content);
        response.put("updatedBy", authentication.getName());
        return ResponseEntity.ok(response);
    }
}
