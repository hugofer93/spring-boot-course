package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal para demostrar filtros e interceptores.
 * 
 * Este proyecto muestra cómo implementar:
 * - Filtros HTTP para logging y auditoría de requests
 * - Interceptores para trazabilidad de métodos
 * - Servicios de auditoría simples
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
