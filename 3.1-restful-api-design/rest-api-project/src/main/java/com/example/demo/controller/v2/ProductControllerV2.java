package com.example.demo.controller.v2;

import com.example.demo.dto.v2.ProductRequestDTO;
import com.example.demo.dto.v2.ProductResponseDTO;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para productos - Versión 2 de la API.
 * 
 * Endpoints disponibles:
 * - GET    /api/v2/products       - Listar todos los productos
 * - GET    /api/v2/products/{id}  - Obtener un producto por ID
 * - POST   /api/v2/products       - Crear un nuevo producto
 * - PUT    /api/v2/products/{id}  - Actualizar un producto
 * - DELETE /api/v2/products/{id}  - Eliminar un producto
 * 
 * VERSIÓN MEJORADA: Esta versión incluye el campo "category" que no existía en v1.
 * 
 * Ejemplo de versionamiento de API:
 * - v1 sigue funcionando para clientes que no han migrado
 * - v2 ofrece nuevas funcionalidades sin romper v1
 * - Los clientes pueden migrar gradualmente
 */
@RestController
@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
public class ProductControllerV2 {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductResponseDTO> responseDTOs = products.stream()
                .map(productMapper::toResponseDTOV2)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(productMapper.toResponseDTOV2(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        Product product = productMapper.toEntityV2(requestDTO);
        Product savedProduct = productService.save(product);
        ProductResponseDTO responseDTO = productMapper.toResponseDTOV2(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        Product product = productMapper.toEntityV2(requestDTO);
        Product updatedProduct = productService.update(id, product);
        ProductResponseDTO responseDTO = productMapper.toResponseDTOV2(updatedProduct);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
