package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot
 * 
 * @SpringBootApplication es una anotación de conveniencia que incluye:
 * - @Configuration: Marca la clase como fuente de definiciones de beans
 * - @EnableAutoConfiguration: Habilita la configuración automática de Spring Boot
 * - @ComponentScan: Escanea el paquete y subpaquetes en busca de componentes
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
