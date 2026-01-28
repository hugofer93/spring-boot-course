package com.example.orderservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para el endpoint raíz.
 * 
 * Proporciona información sobre el servicio y los endpoints disponibles.
 * Este servicio demuestra el uso de Feign Client para comunicarse con otros servicios.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Order Service");
        response.put("message", "Microservicio para gestión de pedidos - Usa Feign Client para comunicarse con User Service");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("port", 8082);
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /", "Información del servicio (este endpoint)");
        endpoints.put("GET /orders", "Obtiene todos los pedidos (con información de usuario vía Feign)");
        endpoints.put("GET /orders/{id}", "Obtiene un pedido por ID (con información de usuario vía Feign)");
        endpoints.put("POST /orders", "Crea un nuevo pedido (valida usuario vía Feign)");
        endpoints.put("GET /orders/users", "Obtiene usuarios usando Feign Client directamente");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> description = new HashMap<>();
        description.put("GET /orders", "Retorna todos los pedidos enriquecidos con información del usuario obtenida del User Service usando Feign Client");
        description.put("GET /orders/{id}", "Retorna un pedido específico con información del usuario obtenida vía Feign Client");
        description.put("POST /orders", "Crea un nuevo pedido. Valida que el usuario exista usando Feign Client. Body: {userId, productName, amount}");
        description.put("GET /orders/users", "Ejemplo directo de uso de Feign Client - Obtiene usuarios del User Service");
        
        response.put("endpointDescriptions", description);
        
        Map<String, Object> feignClient = new HashMap<>();
        feignClient.put("note", "Este servicio consume el User Service usando Spring Cloud OpenFeign");
        feignClient.put("consumedService", "user-service");
        feignClient.put("implementation", "Ver UserClient.java para la interfaz @FeignClient");
        feignClient.put("configuration", "Ver FeignConfig.java para configuración personalizada");
        
        response.put("feignClient", feignClient);
        
        Map<String, String> examples = new HashMap<>();
        examples.put("Get all orders", "curl http://localhost:8082/orders");
        examples.put("Get order by ID", "curl http://localhost:8082/orders/1");
        examples.put("Create order", "curl -X POST http://localhost:8082/orders -H 'Content-Type: application/json' -d '{\"userId\":1,\"productName\":\"Laptop\",\"amount\":999.99}'");
        examples.put("Get users via Feign", "curl http://localhost:8082/orders/users");
        
        response.put("examples", examples);
        
        return ResponseEntity.ok(response);
    }
}
