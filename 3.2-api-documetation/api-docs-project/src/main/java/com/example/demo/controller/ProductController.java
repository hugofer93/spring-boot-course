package com.example.demo.controller;

import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de productos.
 * 
 * Este controlador expone endpoints para realizar operaciones CRUD
 * sobre productos. Todos los endpoints están documentados con Swagger/OpenAPI 3.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(
    name = "Productos",
    description = "API para gestión de productos. Permite crear, leer, actualizar y eliminar productos."
)
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene una lista de todos los productos disponibles en el sistema. " +
                     "La lista incluye información completa de cada producto."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProductResponseDTO.class)
        )
    )
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener un producto por ID",
        description = "Obtiene la información detallada de un producto específico " +
                     "usando su ID único."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content
        )
    })
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(
                description = "ID único del producto",
                required = true,
                example = "1"
            )
            @PathVariable Long id) {
        ProductResponseDTO product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(
        summary = "Crear un nuevo producto",
        description = "Crea un nuevo producto en el sistema. " +
                     "Todos los campos son obligatorios y serán validados."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos proporcionados",
            content = @Content
        )
    })
    public ResponseEntity<ProductResponseDTO> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del producto a crear",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = ProductRequestDTO.class)
                )
            )
            @Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO created = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar un producto existente",
        description = "Actualiza la información de un producto existente. " +
                     "Se requiere el ID del producto y los nuevos datos."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos proporcionados",
            content = @Content
        )
    })
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(
                description = "ID único del producto a actualizar",
                required = true,
                example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevos datos del producto",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = ProductRequestDTO.class)
                )
            )
            @Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO updated = productService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina un producto del sistema usando su ID. " +
                     "Esta operación no se puede deshacer."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Producto eliminado exitosamente",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(
                description = "ID único del producto a eliminar",
                required = true,
                example = "1"
            )
            @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
