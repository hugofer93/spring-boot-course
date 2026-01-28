package com.example.demo.mapper;

import com.example.demo.dto.v1.ProductRequestDTO;
import com.example.demo.dto.v1.ProductResponseDTO;
import com.example.demo.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre entidades y DTOs usando MapStruct.
 * 
 * MapStruct genera código en tiempo de compilación, lo que lo hace:
 * - Muy rápido (sin reflexión en tiempo de ejecución)
 * - Type-safe (errores en tiempo de compilación)
 * - Fácil de depurar (código generado visible)
 * 
 * Este mapper maneja las conversiones para ambas versiones de la API.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // ========== VERSIÓN 1 ==========
    
    /**
     * Convierte ProductRequestDTO (v1) a entidad Product.
     * Ignora campos que no existen en la entidad (como category en v1).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(com.example.demo.dto.v1.ProductRequestDTO dto);

    /**
     * Convierte entidad Product a ProductResponseDTO (v1).
     * MapStruct automáticamente mapea solo los campos que existen en el DTO.
     * Como v1 no incluye updatedAt, simplemente no se mapea.
     */
    com.example.demo.dto.v1.ProductResponseDTO toResponseDTO(Product product);

    // ========== VERSIÓN 2 ==========
    
    /**
     * Convierte ProductRequestDTO (v2) a entidad Product.
     * En v2, el campo "category" se almacena en description temporalmente
     * (en un caso real, agregarías el campo a la entidad).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "description", expression = "java(dto.getDescription() + \" [Category: \" + dto.getCategory() + \"]\")")
    Product toEntityV2(com.example.demo.dto.v2.ProductRequestDTO dto);

    /**
     * Convierte entidad Product a ProductResponseDTO (v2).
     * Extrae la categoría del description y limpia la descripción.
     * 
     * MapStruct mapea automáticamente los campos con el mismo nombre (id, name, price, stock, createdAt, updatedAt).
     * Solo especificamos expresiones para category y description que requieren transformación.
     */
    @Mapping(target = "category", expression = "java(extractCategory(product.getDescription()))")
    @Mapping(target = "description", expression = "java(cleanDescription(product.getDescription()))")
    com.example.demo.dto.v2.ProductResponseDTO toResponseDTOV2(Product product);
    
    /**
     * Método helper para limpiar la descripción removiendo la categoría.
     * Esto asegura que la descripción en el DTO no incluya el formato [Category: ...].
     * 
     * @Named indica a MapStruct que este método solo debe usarse cuando se referencie explícitamente,
     * evitando que se use automáticamente para otros campos String.
     */
    @Named("cleanDescription")
    default String cleanDescription(String description) {
        if (description != null && description.contains("[Category: ")) {
            int start = description.indexOf("[Category: ");
            return description.substring(0, start).trim();
        }
        return description;
    }

    /**
     * Método helper para extraer categoría del description.
     * En un caso real, esto sería un campo separado en la entidad.
     * 
     * Si el producto no tiene categoría en el formato [Category: ...],
     * devuelve "General" como valor por defecto.
     * 
     * @Named indica a MapStruct que este método solo debe usarse cuando se referencie explícitamente,
     * evitando que se use automáticamente para otros campos String.
     */
    @Named("extractCategory")
    default String extractCategory(String description) {
        if (description != null && description.contains("[Category: ")) {
            int start = description.indexOf("[Category: ") + 11;
            int end = description.indexOf("]", start);
            if (end > start) {
                return description.substring(start, end);
            }
        }
        return "General"; // Valor por defecto cuando no hay categoría
    }
}
