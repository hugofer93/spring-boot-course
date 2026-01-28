package com.example.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Product Service - Microservicio para gestión de productos
 * 
 * Este servicio se registra en Eureka para que otros servicios puedan descubrirlo.
 * 
 * Nota: En Spring Cloud 2023.0.0+, el cliente de Eureka se habilita automáticamente
 * cuando detecta la dependencia spring-cloud-starter-netflix-eureka-client.
 */
@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
