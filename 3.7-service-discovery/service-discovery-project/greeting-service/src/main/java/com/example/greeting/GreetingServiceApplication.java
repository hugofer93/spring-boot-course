package com.example.greeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio de saludo que se registra en Eureka.
 * Expone un endpoint simple para demostrar el descubrimiento por parte de otros servicios.
 */
@SpringBootApplication
public class GreetingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreetingServiceApplication.class, args);
    }
}
