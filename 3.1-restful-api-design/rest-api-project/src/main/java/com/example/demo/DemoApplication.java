package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal del proyecto de ejemplo de APIs RESTful avanzadas.
 * 
 * Este proyecto demuestra:
 * - Versionamiento de APIs (v1, v2)
 * - Uso de DTOs para separar capas
 * - Mappers con MapStruct para conversión entre entidades y DTOs
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
