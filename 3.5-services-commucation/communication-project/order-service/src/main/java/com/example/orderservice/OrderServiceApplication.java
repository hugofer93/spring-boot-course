package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Order Service - Microservicio para gestión de pedidos
 * 
 * Este servicio consume el user-service usando Feign Client.
 * 
 * @EnableFeignClients: Habilita el escaneo de interfaces anotadas con @FeignClient
 */
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
