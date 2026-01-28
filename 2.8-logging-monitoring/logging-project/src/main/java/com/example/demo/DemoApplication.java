package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal de demostración de logging.
 * 
 * Este proyecto muestra cómo usar SLF4J y Logback para
 * logging y monitoreo en aplicaciones Spring Boot.
 */
@SpringBootApplication
public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        logger.info("=== Iniciando aplicación de logging ===");
        logger.debug("Argumentos recibidos: {}", (Object) args);
        
        SpringApplication.run(DemoApplication.class, args);
        
        logger.info("=== Aplicación iniciada correctamente ===");
        logger.info("API disponible en: http://localhost:8080");
    }
}
