package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.User;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de pedidos
 * 
 * Este controlador demuestra cómo usar Feign Client indirectamente
 * a través del servicio, y también muestra un ejemplo directo.
 */
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("GET /orders - Obteniendo todos los pedidos");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        log.info("GET /orders/{} - Obteniendo pedido por ID", id);
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        log.info("POST /orders - Creando nuevo pedido para usuario: {}", order.getUserId());
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint de ejemplo que muestra el uso directo de Feign Client
     * para obtener usuarios del user-service
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("GET /orders/users - Obteniendo usuarios desde user-service usando Feign Client");
        return ResponseEntity.ok(orderService.getAllUsers());
    }
}
