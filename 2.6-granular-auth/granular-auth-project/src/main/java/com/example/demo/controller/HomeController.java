package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para el endpoint raíz.
 * 
 * Proporciona información sobre la API y los roles disponibles.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Autorización Granular");
        response.put("description", "Demostración de roles jerárquicos y autorización granular");
        
        Map<String, String> roles = new HashMap<>();
        roles.put("ROLE_USER", "Usuario básico - Acceso limitado");
        roles.put("ROLE_MODERATOR", "Moderador - Puede editar contenido");
        roles.put("ROLE_ADMIN", "Administrador - Acceso completo");
        
        response.put("roles", roles);
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/public/info", "Público - No requiere autenticación");
        endpoints.put("POST /api/auth/login", "Público - Obtener token JWT");
        endpoints.put("GET /api/user/profile", "Usuario autenticado - Cualquier rol (requiere JWT)");
        endpoints.put("GET /api/user/dashboard", "Usuario autenticado - Rol USER o superior (requiere JWT)");
        endpoints.put("GET /api/moderator/content", "Moderador - Rol MODERATOR o ADMIN (requiere JWT)");
        endpoints.put("POST /api/moderator/content", "Moderador - Rol MODERATOR o ADMIN (requiere JWT)");
        endpoints.put("GET /api/admin/users", "Administrador - Solo rol ADMIN (requiere JWT)");
        endpoints.put("DELETE /api/admin/users/{id}", "Administrador - Solo rol ADMIN (requiere JWT)");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> usage = new HashMap<>();
        usage.put("1", "Hacer login con POST /api/auth/login para obtener token JWT");
        usage.put("2", "Usar el token en header: Authorization: Bearer <token>");
        usage.put("3", "Acceder a endpoints protegidos según tu rol");
        
        response.put("usage", usage);
        
        Map<String, String> users = new HashMap<>();
        users.put("user", "password - Rol: USER");
        users.put("moderator", "password - Rol: MODERATOR");
        users.put("admin", "password - Rol: ADMIN");
        
        response.put("testUsers", users);
        
        return ResponseEntity.ok(response);
    }
}
