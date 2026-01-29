package com.example.greeting.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador simple que expone un saludo.
 * Sirve como objetivo del descubrimiento: demo-client lo llama por nombre (greeting-service).
 */
@RestController
@RequestMapping("/api")
public class GreetingController {

    @Value("${spring.application.name:greeting-service}")
    private String applicationName;

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of(
                "message", "Hola desde el servicio de saludo",
                "service", applicationName
        ));
    }
}
