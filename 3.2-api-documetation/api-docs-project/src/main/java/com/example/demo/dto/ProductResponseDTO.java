package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de un producto.
 * 
 * Este DTO se usa en las respuestas de los endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de un producto")
public class ProductResponseDTO {

    @Schema(description = "ID único del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Laptop Dell XPS 15")
    private String name;

    @Schema(description = "Descripción del producto", example = "Laptop de alta gama con procesador Intel i7 y 16GB RAM")
    private String description;

    @Schema(description = "Precio del producto en dólares", example = "1299.99")
    private BigDecimal price;

    @Schema(description = "Cantidad disponible en inventario", example = "10")
    private Integer stock;

    @Schema(description = "Fecha y hora de creación del producto", example = "2026-01-28T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2026-01-28T10:00:00")
    private LocalDateTime updatedAt;
}
