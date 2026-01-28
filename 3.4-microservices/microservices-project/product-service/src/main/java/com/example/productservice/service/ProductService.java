package com.example.productservice.service;

import com.example.productservice.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio para gestión de productos
 */
@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ProductService() {
        // Datos de ejemplo
        products.add(new Product(idGenerator.getAndIncrement(), 
                "Laptop", "Laptop de alta gama", new BigDecimal("1299.99")));
        products.add(new Product(idGenerator.getAndIncrement(), 
                "Mouse", "Mouse inalámbrico", new BigDecimal("29.99")));
        products.add(new Product(idGenerator.getAndIncrement(), 
                "Teclado", "Teclado mecánico", new BigDecimal("89.99")));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public Product createProduct(Product product) {
        product.setId(idGenerator.getAndIncrement());
        products.add(product);
        return product;
    }
}
