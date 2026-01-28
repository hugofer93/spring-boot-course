package com.example.demo.controller;

import com.example.demo.service.calculator.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para operaciones de calculadora.
 * 
 * Demuestra el uso de código limpio en controladores REST.
 */
@RestController
@RequestMapping("/api/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    /**
     * Realiza una operación matemática.
     * 
     * Ejemplo: GET /api/calculator?firstNumber=10&secondNumber=5&operation=ADD
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> calculate(
            @RequestParam double firstNumber,
            @RequestParam double secondNumber,
            @RequestParam CalculatorService.Operation operation) {
        
        try {
            double result = calculatorService.calculate(firstNumber, secondNumber, operation);
            
            Map<String, Object> response = new HashMap<>();
            response.put("firstNumber", firstNumber);
            response.put("secondNumber", secondNumber);
            response.put("operation", operation);
            response.put("result", result);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
