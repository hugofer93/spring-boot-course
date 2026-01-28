package com.example.demo.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de producto (Versión 2 de la API).
 * 
 * VERSIÓN MEJORADA: Incluye el campo "category" y mejor formato de fechas.
 * 
 * Ejemplo de evolución de API:
 * - v1: Solo datos básicos
 * - v2: Agrega categoría y mejor estructura
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category; // NUEVO en v2
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // NUEVO en v2
}
