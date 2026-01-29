package com.example.democlient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente de demostración: se registra en Eureka y usa el registro para
 * descubrir y llamar a greeting-service por nombre (sin conocer host ni puerto).
 */
@SpringBootApplication
public class DemoClientApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoClientApplication.class, args);
    }
}
