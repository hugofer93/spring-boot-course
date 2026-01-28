package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para usuarios autenticados.
 * 
 * Estos endpoints requieren autenticación, pero cualquier rol puede acceder.
 * Se usa @PreAuthorize para autorización granular.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * Endpoint accesible para cualquier usuario autenticado.
     * 
     * No se especifica rol, solo requiere autenticación.
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Perfil del usuario");
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("access", "Requiere autenticación (cualquier rol)");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint con autorización granular usando @PreAuthorize.
     * 
     * Solo usuarios con rol USER o superior pueden acceder.
     * hasRole('USER') permite USER, MODERATOR y ADMIN.
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Dashboard del usuario");
        response.put("username", authentication.getName());
        response.put("access", "Requiere rol USER o superior");
        return ResponseEntity.ok(response);
    }
}
