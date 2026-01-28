package com.example.demo.service.calculator;

import org.springframework.stereotype.Service;

/**
 * EJEMPLO DE CÓDIGO "BUENO" - SIGUIENDO PRINCIPIOS CLEAN CODE
 * 
 * Este servicio demuestra:
 * - Nombres descriptivos y claros
 * - Funciones pequeñas con una sola responsabilidad
 * - Sin duplicación de código (DRY)
 * - Validación de entrada
 * - Manejo de errores apropiado
 */
@Service
public class CalculatorService {

    /**
     * Realiza una operación matemática entre dos números.
     * 
     * @param firstNumber Primer número
     * @param secondNumber Segundo número
     * @param operation Operación a realizar (ADD, SUBTRACT, MULTIPLY, DIVIDE)
     * @return Resultado de la operación
     * @throws IllegalArgumentException Si la operación no es válida o hay división por cero
     */
    public double calculate(double firstNumber, double secondNumber, Operation operation) {
        validateInputs(firstNumber, secondNumber, operation);
        
        return switch (operation) {
            case ADD -> add(firstNumber, secondNumber);
            case SUBTRACT -> subtract(firstNumber, secondNumber);
            case MULTIPLY -> multiply(firstNumber, secondNumber);
            case DIVIDE -> divide(firstNumber, secondNumber);
        };
    }

    private double add(double firstNumber, double secondNumber) {
        return firstNumber + secondNumber;
    }

    private double subtract(double firstNumber, double secondNumber) {
        return firstNumber - secondNumber;
    }

    private double multiply(double firstNumber, double secondNumber) {
        return firstNumber * secondNumber;
    }

    private double divide(double firstNumber, double secondNumber) {
        if (secondNumber == 0) {
            throw new IllegalArgumentException("No se puede dividir por cero");
        }
        return firstNumber / secondNumber;
    }

    private void validateInputs(double firstNumber, double secondNumber, Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("La operación no puede ser nula");
        }
    }

    /**
     * Enum para las operaciones matemáticas disponibles.
     * Usar enum en lugar de String previene errores y hace el código más seguro.
     */
    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }
}
