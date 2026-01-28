package com.example.demo.controller;

import com.example.demo.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador que demuestra logging en diferentes escenarios.
 */
@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class ExampleController {

    private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);
    
    private final ExampleService exampleService;

    @GetMapping("/process")
    public ResponseEntity<Map<String, String>> processData(@RequestParam(required = false) String input) {
        logger.info("Solicitud de procesamiento recibida. Input: {}", input);
        
        String result = exampleService.processData(input);
        
        logger.info("Procesamiento completado. Resultado: {}", result);
        return ResponseEntity.ok(Map.of(
            "input", input != null ? input : "null",
            "result", result
        ));
    }

    @GetMapping("/expensive")
    public ResponseEntity<Map<String, String>> expensiveOperation() {
        logger.info("Solicitud de operación costosa recibida");
        
        exampleService.performExpensiveOperation();
        
        return ResponseEntity.ok(Map.of(
            "message", "Operación costosa completada",
            "note", "Revisa los logs para ver el tiempo de ejecución"
        ));
    }

    @GetMapping("/conditional")
    public ResponseEntity<Map<String, String>> conditionalLogging(
            @RequestParam(defaultValue = "true") boolean condition) {
        logger.info("Solicitud de logging condicional. Condición: {}", condition);
        
        exampleService.conditionalLogging(condition);
        
        return ResponseEntity.ok(Map.of(
            "message", "Logging condicional ejecutado",
            "condition", String.valueOf(condition)
        ));
    }
}
