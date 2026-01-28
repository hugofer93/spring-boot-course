package com.example.demo.controller;

import com.example.demo.service.CalculatorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas para CalculatorController usando MockMvc con mocks.
 * 
 * @WebMvcTest:
 * - Carga solo el contexto web de Spring (más rápido que @SpringBootTest)
 * - Permite probar controladores de forma aislada
 * - Requiere mockear las dependencias del controlador
 * 
 * @MockBean:
 * - Crea un mock del servicio para aislar el controlador
 * - Permite controlar el resultado del servicio en las pruebas
 * - Útil cuando queremos probar solo el controlador sin depender del servicio real
 * 
 * IMPORTANTE: Los mocks solo se usan para aislar dependencias y controlar resultados.
 * Solo evaluamos el resultado final, no verificamos comportamiento (no usamos verify()).
 */
@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    @Test
    @DisplayName("GET /api/calculator/add debería retornar la suma correctamente")
    void testAdd() throws Exception {
        // Configurar el mock para que retorne un resultado específico
        when(calculatorService.add(5.0, 3.0)).thenReturn(8.0);

        // Ejecutar la petición y evaluar solo el resultado
        mockMvc.perform(get("/api/calculator/add")
                        .param("a", "5.0")
                        .param("b", "3.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.a").value(5.0))
                .andExpect(jsonPath("$.b").value(3.0))
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.result").value(8.0));
    }

    @Test
    @DisplayName("GET /api/calculator/subtract debería retornar la resta correctamente")
    void testSubtract() throws Exception {
        when(calculatorService.subtract(10.0, 4.0)).thenReturn(6.0);

        mockMvc.perform(get("/api/calculator/subtract")
                        .param("a", "10.0")
                        .param("b", "4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(6.0))
                .andExpect(jsonPath("$.operation").value("subtract"));
    }

    @Test
    @DisplayName("GET /api/calculator/multiply debería retornar la multiplicación correctamente")
    void testMultiply() throws Exception {
        when(calculatorService.multiply(5.0, 4.0)).thenReturn(20.0);

        mockMvc.perform(get("/api/calculator/multiply")
                        .param("a", "5.0")
                        .param("b", "4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(20.0))
                .andExpect(jsonPath("$.operation").value("multiply"));
    }

    @Test
    @DisplayName("GET /api/calculator/divide debería retornar la división correctamente")
    void testDivide_Success() throws Exception {
        when(calculatorService.divide(10.0, 2.0)).thenReturn(5.0);

        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10.0")
                        .param("b", "2.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(5.0))
                .andExpect(jsonPath("$.operation").value("divide"));
    }

    @Test
    @DisplayName("GET /api/calculator/divide debería retornar error 400 al dividir por cero")
    void testDivide_ByZero() throws Exception {
        // Configurar el mock para que lance una excepción
        when(calculatorService.divide(10.0, 0.0))
                .thenThrow(new IllegalArgumentException("No se puede dividir por cero"));

        // Evaluar solo el resultado: status y mensaje de error
        mockMvc.perform(get("/api/calculator/divide")
                        .param("a", "10.0")
                        .param("b", "0.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No se puede dividir por cero"));
    }

    @Test
    @DisplayName("GET /api/calculator/square/{number} debería retornar el cuadrado correctamente")
    void testSquare() throws Exception {
        when(calculatorService.square(5.0)).thenReturn(25.0);

        mockMvc.perform(get("/api/calculator/square/5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(5.0))
                .andExpect(jsonPath("$.operation").value("square"))
                .andExpect(jsonPath("$.result").value(25.0));
    }
}
