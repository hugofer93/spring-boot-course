package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CalculatorService.
 * 
 * Las pruebas unitarias se enfocan en probar la lógica de negocio
 * de forma aislada, sin depender de otros componentes.
 * 
 * Características:
 * - Rápidas de ejecutar
 * - No requieren contexto de Spring
 * - Prueban una unidad de código a la vez
 */
class CalculatorServiceTest {

    private CalculatorService calculatorService;

    /**
     * Se ejecuta antes de cada prueba.
     * Crea una nueva instancia del servicio para cada test.
     */
    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    @DisplayName("Debería sumar dos números positivos correctamente")
    void testAdd_PositiveNumbers() {
        // Arrange (Preparar)
        double a = 5.0;
        double b = 3.0;
        double expected = 8.0;

        // Act (Ejecutar)
        double result = calculatorService.add(a, b);

        // Assert (Verificar)
        assertEquals(expected, result, 0.001, "La suma de 5 + 3 debe ser 8");
    }

    @Test
    @DisplayName("Debería sumar números negativos correctamente")
    void testAdd_NegativeNumbers() {
        double result = calculatorService.add(-5.0, -3.0);
        assertEquals(-8.0, result, 0.001);
    }

    @Test
    @DisplayName("Debería restar dos números correctamente")
    void testSubtract() {
        double result = calculatorService.subtract(10.0, 4.0);
        assertEquals(6.0, result, 0.001);
    }

    @Test
    @DisplayName("Debería multiplicar dos números correctamente")
    void testMultiply() {
        double result = calculatorService.multiply(5.0, 4.0);
        assertEquals(20.0, result, 0.001);
    }

    @Test
    @DisplayName("Debería dividir dos números correctamente")
    void testDivide_ValidDivision() {
        double result = calculatorService.divide(10.0, 2.0);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    @DisplayName("Debería lanzar excepción al dividir por cero")
    void testDivide_ByZero() {
        // Verificar que se lanza IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculatorService.divide(10.0, 0.0),
                "Debería lanzar IllegalArgumentException al dividir por cero"
        );

        // Verificar el mensaje de la excepción
        assertEquals("No se puede dividir por cero", exception.getMessage());
    }

    @Test
    @DisplayName("Debería calcular el cuadrado de un número correctamente")
    void testSquare() {
        double result = calculatorService.square(5.0);
        assertEquals(25.0, result, 0.001);
    }

    @Test
    @DisplayName("Debería manejar números decimales correctamente")
    void testAdd_DecimalNumbers() {
        double result = calculatorService.add(2.5, 3.7);
        assertEquals(6.2, result, 0.001);
    }
}
