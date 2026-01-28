package com.example.demo.service.calculator;

import org.springframework.stereotype.Service;

/**
 * EJEMPLO DE CÓDIGO "MALO" - NO USAR EN PRODUCCIÓN
 * 
 * Este servicio demuestra violaciones de principios Clean Code:
 * - Nombres poco descriptivos
 * - Funciones muy largas
 * - Lógica duplicada
 * - Falta de validación
 * - Comentarios innecesarios
 */
@Service
public class BadCalculatorService {

    // Método mal nombrado y muy largo
    public double calc(double a, double b, String op) {
        // Suma
        if (op.equals("+")) {
            return a + b;
        }
        // Resta
        if (op.equals("-")) {
            return a - b;
        }
        // Multiplicación
        if (op.equals("*")) {
            return a * b;
        }
        // División
        if (op.equals("/")) {
            return a / b;
        }
        return 0;
    }

    // Código duplicado y nombres poco claros
    public double process(double x, double y) {
        double result = 0;
        result = x + y;
        result = result * 2;
        result = result - 10;
        return result;
    }

    // Múltiples responsabilidades en un solo método
    public String doEverything(double num1, double num2, String operation, boolean format) {
        double res = 0;
        if (operation.equals("add")) {
            res = num1 + num2;
        } else if (operation.equals("subtract")) {
            res = num1 - num2;
        } else if (operation.equals("multiply")) {
            res = num1 * num2;
        } else if (operation.equals("divide")) {
            res = num1 / num2;
        }
        if (format) {
            return String.format("%.2f", res);
        }
        return String.valueOf(res);
    }
}
