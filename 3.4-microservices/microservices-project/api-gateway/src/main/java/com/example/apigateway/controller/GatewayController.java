package com.example.apigateway.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Controlador del API Gateway
 * 
 * Nota: Spring Cloud Gateway usa WebFlux (reactivo), no Spring MVC
 */
@RestController
@RequestMapping
public class GatewayController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> home() {
        return Mono.just(Map.of(
            "message", "API Gateway - Microservicios con Spring Cloud",
            "status", "running",
            "endpoints", Map.of(
                "users", "/api/users",
                "products", "/api/products",
                "eureka", "http://localhost:8761"
            )
        ));
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, String>> health() {
        return Mono.just(Map.of("status", "UP"));
    }
}
