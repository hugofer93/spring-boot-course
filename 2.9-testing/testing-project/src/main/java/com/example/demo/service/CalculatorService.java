package com.example.demo.service;

import org.springframework.stereotype.Service;

/**
 * Servicio de cálculo simple.
 * 
 * Este servicio contiene lógica de negocio que será probada
 * con pruebas unitarias.
 */
@Service
public class CalculatorService {

    /**
     * Suma dos números.
     * 
     * @param a Primer número
     * @param b Segundo número
     * @return Suma de a y b
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Resta dos números.
     * 
     * @param a Minuendo
     * @param b Sustraendo
     * @return Diferencia de a y b
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplica dos números.
     * 
     * @param a Primer número
     * @param b Segundo número
     * @return Producto de a y b
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divide dos números.
     * 
     * @param a Dividendo
     * @param b Divisor
     * @return Cociente de a y b
     * @throws IllegalArgumentException si el divisor es cero
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("No se puede dividir por cero");
        }
        return a / b;
    }

    /**
     * Calcula el cuadrado de un número.
     * 
     * @param number Número a elevar al cuadrado
     * @return Cuadrado del número
     */
    public double square(double number) {
        return number * number;
    }
}
