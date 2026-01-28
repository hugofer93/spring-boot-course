package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User Service - Microservicio para gestión de usuarios
 * 
 * Este servicio se registra en Eureka y expone endpoints REST
 * que serán consumidos por otros servicios usando Feign Client.
 */
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
