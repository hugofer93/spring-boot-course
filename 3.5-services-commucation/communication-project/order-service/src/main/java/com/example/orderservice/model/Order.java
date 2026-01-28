package com.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Modelo de Pedido
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private String productName;
    private Double amount;
    private LocalDateTime orderDate;
    private String status;
    
    // Información del usuario obtenida del user-service
    private String userName;
    private String userEmail;
}
