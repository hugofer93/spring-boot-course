package com.example.demo.controller;

import com.example.demo.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para operaciones de cálculo.
 * 
 * Este controlador será probado con pruebas de integración.
 */
@RestController
@RequestMapping("/api/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    /**
     * Endpoint para sumar dos números.
     * 
     * @param a Primer número
     * @param b Segundo número
     * @return Resultado de la suma
     */
    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> add(
            @RequestParam double a,
            @RequestParam double b) {
        double result = calculatorService.add(a, b);
        return ResponseEntity.ok(createResponse(a, b, result, "add"));
    }

    /**
     * Endpoint para restar dos números.
     * 
     * @param a Minuendo
     * @param b Sustraendo
     * @return Resultado de la resta
     */
    @GetMapping("/subtract")
    public ResponseEntity<Map<String, Object>> subtract(
            @RequestParam double a,
            @RequestParam double b) {
        double result = calculatorService.subtract(a, b);
        return ResponseEntity.ok(createResponse(a, b, result, "subtract"));
    }

    /**
     * Endpoint para multiplicar dos números.
     * 
     * @param a Primer número
     * @param b Segundo número
     * @return Resultado de la multiplicación
     */
    @GetMapping("/multiply")
    public ResponseEntity<Map<String, Object>> multiply(
            @RequestParam double a,
            @RequestParam double b) {
        double result = calculatorService.multiply(a, b);
        return ResponseEntity.ok(createResponse(a, b, result, "multiply"));
    }

    /**
     * Endpoint para dividir dos números.
     * 
     * @param a Dividendo
     * @param b Divisor
     * @return Resultado de la división
     */
    @GetMapping("/divide")
    public ResponseEntity<Map<String, Object>> divide(
            @RequestParam double a,
            @RequestParam double b) {
        try {
            double result = calculatorService.divide(a, b);
            return ResponseEntity.ok(createResponse(a, b, result, "divide"));
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Endpoint para calcular el cuadrado de un número.
     * 
     * @param number Número a elevar al cuadrado
     * @return Resultado del cuadrado
     */
    @GetMapping("/square/{number}")
    public ResponseEntity<Map<String, Object>> square(@PathVariable double number) {
        double result = calculatorService.square(number);
        Map<String, Object> response = new HashMap<>();
        response.put("number", number);
        response.put("operation", "square");
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    /**
     * Crea una respuesta estándar para operaciones binarias.
     */
    private Map<String, Object> createResponse(double a, double b, double result, String operation) {
        Map<String, Object> response = new HashMap<>();
        response.put("a", a);
        response.put("b", b);
        response.put("operation", operation);
        response.put("result", result);
        return response;
    }
}
