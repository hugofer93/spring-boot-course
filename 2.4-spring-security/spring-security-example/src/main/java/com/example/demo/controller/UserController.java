package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Endpoint Público: Crear usuario
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // IMPORTANTE: Encriptar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Asignar rol por defecto si no viene
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Endpoint Privado: Listar usuarios
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}