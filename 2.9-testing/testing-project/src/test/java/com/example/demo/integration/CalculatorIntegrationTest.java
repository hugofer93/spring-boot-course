package com.example.demo.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración completas usando @SpringBootTest.
 * 
 * @SpringBootTest:
 * - Carga el contexto completo de Spring Boot
 * - Incluye todos los beans y configuraciones
 * - Más lento pero más realista
 * - Prueba la integración real entre componentes
 * 
 * @AutoConfigureMockMvc:
 * - Configura MockMvc automáticamente
 * - Permite probar los endpoints HTTP sin levantar un servidor real
 * 
 * NOTA: En estas pruebas usamos el servicio REAL (no mocks).
 * Esto es útil cuando queremos probar el flujo completo desde el controlador
 * hasta el servicio, asegurando que todo funcione correctamente juntos.
 * 
 * Si necesitáramos aislar alguna dependencia externa (base de datos, APIs externas, etc.),
 * usaríamos @MockBean para mockear solo esas dependencias específicas.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CalculatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Integración completa: Suma de dos números")
    void testAdd_Integration() throws Exception {
        mockMvc.perform(get("/api/calculator/add")
                        .param("a", "10.0")
                        .param("b", "5.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.a").value(10.0))
                .andExpect(jsonPath("$.b").value(5.0))
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.result").value(15.0));
    }

    @Test
    @DisplayName("Integración completa: Resta de dos números")
    void testSubtract_Integration() throws Exception {
        mockMvc.perform(get("/api/calculator/subtract")
                        .param("a", "10.0")
                        .param("b", "3.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(7.0))
                .andExpect(jsonPath("$.operation").value("subtract"));
    }

    @Test
    @DisplayName("Integración completa: Multiplicación de dos números")
    void testMultiply_Integration() throws Exception {
        mockMvc.perform(get("/api/calculator/multiply")
                        .param("a", "6.0")
                        .param("b", "7.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(42.0))
                .andExpect(jsonPath("$.operation").value("multiply"));
    }

    @Test
    @DisplayName("Integración completa: División de dos números")
    void testDivide_Integration() throws Exception {
        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "20.0")
                        .param("b", "4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(5.0))
                .andExpect(jsonPath("$.operation").value("divide"));
    }

    @Test
    @DisplayName("Integración completa: División por cero debe retornar error")
    void testDivide_ByZero_Integration() throws Exception {
        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10.0")
                        .param("b", "0.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No se puede dividir por cero"));
    }

    @Test
    @DisplayName("Integración completa: Cálculo del cuadrado")
    void testSquare_Integration() throws Exception {
        mockMvc.perform(get("/api/calculator/square/6.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(6.0))
                .andExpect(jsonPath("$.operation").value("square"))
                .andExpect(jsonPath("$.result").value(36.0));
    }

    @Test
    @DisplayName("Integración completa: Endpoint raíz debe retornar información de la API")
    void testHome_Integration() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Testing Project API"))
                .andExpect(jsonPath("$.endpoints").exists());
    }
}
