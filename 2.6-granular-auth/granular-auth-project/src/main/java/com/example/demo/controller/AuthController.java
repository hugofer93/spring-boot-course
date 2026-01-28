package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador simple para autenticación.
 * 
 * Maneja el endpoint de login que permite a los usuarios
 * autenticarse y obtener un token JWT.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Endpoint para autenticar usuarios y obtener token JWT.
     * 
     * @param loginRequest Credenciales del usuario (username y password)
     * @return Token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Autenticar al usuario (valida username y password)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Si llegamos aquí, la autenticación fue exitosa
        // Cargar los detalles del usuario para generar el token
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        // Generar el token JWT
        String token = jwtService.generateToken(userDetails);

        // Devolver el token en la respuesta
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
