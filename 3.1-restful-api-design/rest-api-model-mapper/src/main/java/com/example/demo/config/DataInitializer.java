package com.example.demo.config;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Inicializador de datos de ejemplo.
 * 
 * Crea algunos productos de ejemplo al iniciar la aplicación
 * para facilitar las pruebas.
 */
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                // Producto 1 - Laptop con categoría "Electrónica"
                Product product1 = new Product();
                product1.setName("Laptop");
                product1.setDescription("Laptop de alta gama [Category: Electrónica]");
                product1.setPrice(new BigDecimal("1299.99"));
                product1.setStock(10);
                productRepository.save(product1);

                // Producto 2 - Mouse con categoría "Accesorios"
                Product product2 = new Product();
                product2.setName("Mouse");
                product2.setDescription("Mouse inalámbrico [Category: Accesorios]");
                product2.setPrice(new BigDecimal("29.99"));
                product2.setStock(50);
                productRepository.save(product2);

                // Producto 3 - Teclado con categoría "Accesorios"
                Product product3 = new Product();
                product3.setName("Teclado");
                product3.setDescription("Teclado mecánico [Category: Accesorios]");
                product3.setPrice(new BigDecimal("79.99"));
                product3.setStock(30);
                productRepository.save(product3);

                System.out.println(">>> Productos de ejemplo creados correctamente con categorías");
            }
        };
    }
}
