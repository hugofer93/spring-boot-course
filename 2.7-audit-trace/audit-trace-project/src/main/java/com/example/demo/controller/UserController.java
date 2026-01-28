package com.example.demo.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controlador de ejemplo para usuarios.
 * 
 * Este controlador tiene endpoints simples que serán auditados
 * por los filtros e interceptores.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Simulación de base de datos en memoria
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private long nextId = 1;

    /**
     * Obtener todos los usuarios.
     * 
     * Este endpoint será auditado por el filtro y el interceptor.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(new ArrayList<>(users.values()));
    }

    /**
     * Obtener un usuario por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = users.get(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Crear un nuevo usuario.
     * 
     * Este endpoint será auditado por el filtro y el interceptor.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setId(nextId++);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        users.put(user.getId(), user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Eliminar un usuario.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (users.remove(id) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Clases internas para el ejemplo
    @Data
    public static class User {
        private Long id;
        private String name;
        private String email;
    }

    @Data
    public static class CreateUserRequest {
        private String name;
        private String email;
    }
}
