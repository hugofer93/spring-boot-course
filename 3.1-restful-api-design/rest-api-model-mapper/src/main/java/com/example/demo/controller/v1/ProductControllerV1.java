package com.example.demo.controller.v1;

import com.example.demo.dto.v1.ProductRequestDTO;
import com.example.demo.dto.v1.ProductResponseDTO;
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
 * Controlador REST para productos - Versión 1 de la API.
 * 
 * Endpoints disponibles:
 * - GET    /api/v1/products       - Listar todos los productos
 * - GET    /api/v1/products/{id}  - Obtener un producto por ID
 * - POST   /api/v1/products       - Crear un nuevo producto
 * - PUT    /api/v1/products/{id}  - Actualizar un producto
 * - DELETE /api/v1/products/{id}  - Eliminar un producto
 * 
 * Esta es la versión inicial de la API (v1).
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductControllerV1 {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductResponseDTO> responseDTOs = products.stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(productMapper.toResponseDTO(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        Product product = productMapper.toEntity(requestDTO);
        Product savedProduct = productService.save(product);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        Product product = productMapper.toEntity(requestDTO);
        Product updatedProduct = productService.update(id, product);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(updatedProduct);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
