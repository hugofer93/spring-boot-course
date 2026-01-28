package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway - Punto de entrada único para todos los microservicios
 * 
 * Este servicio actúa como gateway que enruta las peticiones a los microservicios
 * correspondientes usando Service Discovery (Eureka).
 * 
 * Nota: En Spring Cloud 2023.0.0+, el cliente de Eureka se habilita automáticamente
 * cuando detecta la dependencia spring-cloud-starter-netflix-eureka-client.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
