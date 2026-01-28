package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba básica para verificar que el contexto de Spring Boot se carga correctamente.
 * 
 * Esta prueba verifica que:
 * - La aplicación puede iniciarse sin errores
 * - Todos los beans se configuran correctamente
 * - No hay problemas de configuración
 */
@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        // Si este test pasa, significa que el contexto de Spring se cargó correctamente
    }
}
