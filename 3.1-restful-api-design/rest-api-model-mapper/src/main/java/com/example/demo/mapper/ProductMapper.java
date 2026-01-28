package com.example.demo.mapper;

import com.example.demo.dto.v1.ProductRequestDTO;
import com.example.demo.dto.v1.ProductResponseDTO;
import com.example.demo.model.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre entidades y DTOs usando ModelMapper.
 * 
 * ModelMapper usa reflexión en tiempo de ejecución para mapear objetos:
 * - Más flexible que MapStruct (no requiere compilación)
 * - Configuración más simple
 * - Menos performante que MapStruct (usa reflexión)
 * 
 * Este mapper maneja las conversiones para ambas versiones de la API.
 */
@Component
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductMapper() {
        this.modelMapper = new ModelMapper();
        
        // Configurar ModelMapper para usar matching estricto
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
        
        // Configurar mapeos personalizados para v2
        configureV2Mappings();
    }

    // ========== VERSIÓN 1 ==========
    
    /**
     * Convierte ProductRequestDTO (v1) a entidad Product.
     */
    public Product toEntity(ProductRequestDTO dto) {
        Product product = modelMapper.map(dto, Product.class);
        // Ignorar campos que no deben mapearse
        product.setId(null);
        product.setCreatedAt(null);
        product.setUpdatedAt(null);
        return product;
    }

    /**
     * Convierte entidad Product a ProductResponseDTO (v1).
     * Limpia la descripción removiendo el formato [Category: ...] si existe.
     */
    public ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);
        // Limpiar la descripción removiendo la categoría (v1 no debe mostrar category)
        String description = product.getDescription();
        dto.setDescription(cleanDescription(description));
        return dto;
    }

    // ========== VERSIÓN 2 ==========
    
    /**
     * Convierte ProductRequestDTO (v2) a entidad Product.
     * En v2, el campo "category" se almacena en description temporalmente.
     */
    public Product toEntityV2(com.example.demo.dto.v2.ProductRequestDTO dto) {
        Product product = modelMapper.map(dto, Product.class);
        // Ignorar campos que no deben mapearse
        product.setId(null);
        product.setCreatedAt(null);
        product.setUpdatedAt(null);
        
        // Agregar categoría al description
        String description = dto.getDescription();
        String category = dto.getCategory();
        product.setDescription(description + " [Category: " + category + "]");
        
        return product;
    }

    /**
     * Convierte entidad Product a ProductResponseDTO (v2).
     * Extrae la categoría del description y limpia la descripción.
     */
    public com.example.demo.dto.v2.ProductResponseDTO toResponseDTOV2(Product product) {
        com.example.demo.dto.v2.ProductResponseDTO dto = modelMapper.map(product, com.example.demo.dto.v2.ProductResponseDTO.class);
        
        // Extraer categoría del description
        String description = product.getDescription();
        String category = extractCategory(description);
        String cleanDescription = cleanDescription(description);
        
        dto.setCategory(category);
        dto.setDescription(cleanDescription);
        
        return dto;
    }

    /**
     * Configura mapeos personalizados para v2.
     */
    private void configureV2Mappings() {
        // ModelMapper mapea automáticamente campos con el mismo nombre
        // No necesitamos configuración adicional para v2
    }

    /**
     * Método helper para limpiar la descripción removiendo la categoría.
     */
    private String cleanDescription(String description) {
        if (description != null && description.contains("[Category: ")) {
            int start = description.indexOf("[Category: ");
            return description.substring(0, start).trim();
        }
        return description;
    }

    /**
     * Método helper para extraer categoría del description.
     * Si el producto no tiene categoría en el formato [Category: ...],
     * devuelve "General" como valor por defecto.
     */
    private String extractCategory(String description) {
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
