package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para administradores.
 * 
 * Estos endpoints requieren rol ADMIN exclusivamente.
 * Demuestra autorización granular con diferentes expresiones.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * Endpoint para obtener todos los usuarios.
     * 
     * Solo administradores pueden acceder.
     * La autorización se define en SecurityConfig con hasRole("ADMIN").
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Lista de usuarios (solo administradores)");
        response.put("username", authentication.getName());
        response.put("access", "Requiere rol ADMIN exclusivamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para eliminar usuarios.
     * 
     * Usa @PreAuthorize con expresión más específica.
     * hasAuthority permite verificar permisos específicos.
     */
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable Long id,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario eliminado");
        response.put("id", id);
        response.put("deletedBy", authentication.getName());
        response.put("access", "Requiere rol ADMIN exclusivamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para configuraciones del sistema.
     * 
     * Demuestra uso de expresiones complejas en @PreAuthorize.
     */
    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSettings(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Configuraciones del sistema");
        response.put("username", authentication.getName());
        response.put("access", "Solo administradores");
        return ResponseEntity.ok(response);
    }
}
