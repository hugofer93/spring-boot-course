package com.example.demo.dto.v2;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para crear/actualizar un producto (Versión 2 de la API).
 * 
 * VERSIÓN MEJORADA: Ahora incluye un campo adicional "category" que no existía en v1.
 * 
 * Este es un ejemplo de cómo evolucionar una API sin romper compatibilidad:
 * - v1 sigue funcionando con los campos originales
 * - v2 agrega nuevos campos sin afectar a v1
 * - Los clientes pueden migrar gradualmente a v2
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

    // NUEVO CAMPO en v2
    @NotBlank(message = "La categoría es obligatoria")
    private String category;
}
