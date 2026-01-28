package com.example.demo.mapper;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre entidades y DTOs usando MapStruct.
 * 
 * MapStruct genera código en tiempo de compilación, lo que lo hace:
 * - Muy rápido (sin reflexión en tiempo de ejecución)
 * - Type-safe (errores en tiempo de compilación)
 * - Fácil de depurar (código generado visible)
 * 
 * Este mapper maneja las conversiones entre Product y sus DTOs.
 * MapStruct automáticamente mapea campos con el mismo nombre.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Convierte ProductRequestDTO a entidad Product.
     * Ignora campos que no deben mapearse (id, createdAt, updatedAt).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    /**
     * Convierte entidad Product a ProductResponseDTO.
     * MapStruct automáticamente mapea todos los campos con el mismo nombre.
     */
    ProductResponseDTO toResponseDTO(Product product);
}
