package com.example.demo.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador simple que expone un endpoint y registra una métrica personalizada.
 * Las llamadas a /api/ping incrementan el contador demo.operations para ilustrar métricas.
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    private final Counter operationsCounter;

    public DemoController(MeterRegistry meterRegistry) {
        this.operationsCounter = Counter.builder("demo.operations")
                .description("Número de llamadas al endpoint de ejemplo")
                .register(meterRegistry);
    }

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        operationsCounter.increment();
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "pong"
        ));
    }
}
