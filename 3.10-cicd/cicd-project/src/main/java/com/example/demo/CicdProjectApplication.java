package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación de ejemplo para CI/CD con GitHub Actions o Jenkins.
 */
@SpringBootApplication
public class CicdProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CicdProjectApplication.class, args);
    }
}
