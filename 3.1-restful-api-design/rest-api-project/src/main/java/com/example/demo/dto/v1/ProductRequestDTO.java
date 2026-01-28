package com.example.demo.dto.v1;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para crear/actualizar un producto (Versión 1 de la API).
 * 
 * Esta es la versión inicial de la API. Solo incluye campos básicos.
 * 
 * IMPORTANTE: Los DTOs separan la capa de presentación de la capa de dominio.
 * Esto permite:
 * - Cambiar la estructura de la API sin modificar la entidad
 * - Validar datos de entrada de forma independiente
 * - Ocultar información sensible de la entidad
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}
