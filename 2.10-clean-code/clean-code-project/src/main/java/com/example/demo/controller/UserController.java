package com.example.demo.controller;

import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para operaciones de usuario.
 * 
 * Demuestra el uso de código limpio en controladores REST.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Valida información de usuario.
     * 
     * Ejemplo: GET /api/user/validate?name=Juan&age=25&email=juan@example.com
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateUser(
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam String email) {
        
        String result = userService.validateAndProcessUser(name, age, email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        response.put("name", name);
        response.put("age", age);
        response.put("email", email);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Calcula precio final con descuento.
     * 
     * Ejemplo: GET /api/user/price?purchasePrice=150.0
     */
    @GetMapping("/price")
    public ResponseEntity<Map<String, Object>> calculatePrice(
            @RequestParam double purchasePrice) {
        
        double finalPrice = userService.calculateFinalPrice(purchasePrice);
        
        Map<String, Object> response = new HashMap<>();
        response.put("purchasePrice", purchasePrice);
        response.put("finalPrice", finalPrice);
        response.put("discount", purchasePrice - finalPrice);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Formatea información de usuario.
     * 
     * Ejemplo: GET /api/user/format?firstName=Juan&lastName=Pérez&age=25&email=juan@example.com
     */
    @GetMapping("/format")
    public ResponseEntity<Map<String, Object>> formatUserInfo(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam int age,
            @RequestParam String email) {
        
        String formattedInfo = userService.formatUserInformation(firstName, lastName, age, email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("formattedInfo", formattedInfo);
        
        return ResponseEntity.ok(response);
    }
}
