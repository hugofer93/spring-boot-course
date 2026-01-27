package com.example.demo.controller;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * 
 * Este controlador:
 * - Expone endpoints para operaciones CRUD de usuarios
 * - Valida los datos de entrada usando @Valid
 * - Delega la lógica de negocio al UserService
 * - Usa DTOs para separar la capa de presentación de la capa de dominio
 * 
 * Endpoints:
 * - POST /api/users: Crear usuario (público, sin autenticación)
 * - GET /api/users: Listar usuarios (protegido, requiere JWT)
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint público para crear un nuevo usuario.
     * 
     * No requiere autenticación JWT (permitido en SecurityConfig).
     * 
     * @param userRequestDTO Datos del usuario a crear (validados con @Valid)
     * @return UserResponseDTO con los datos del usuario creado (sin password)
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.create(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Endpoint protegido para listar todos los usuarios.
     * 
     * Requiere autenticación JWT (token en header Authorization: Bearer <token>).
     * 
     * @return Lista de UserResponseDTO (sin passwords)
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}