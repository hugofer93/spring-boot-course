package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador que demuestra los endpoints de monitoreo de Actuator.
 * 
 * Este controlador no es necesario para que Actuator funcione,
 * pero ayuda a entender qué endpoints están disponibles.
 */
@RestController
public class MonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringController.class);

    @GetMapping("/api/monitoring")
    public ResponseEntity<Map<String, Object>> monitoringInfo() {
        logger.info("Solicitud de información de monitoreo recibida");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoints de monitoreo disponibles");
        response.put("endpoints", Map.of(
            "health", "/actuator/health - Estado de salud de la aplicación",
            "metrics", "/actuator/metrics - Métricas de la aplicación",
            "info", "/actuator/info - Información de la aplicación"
        ));
        response.put("examples", Map.of(
            "health_check", "curl http://localhost:8080/actuator/health",
            "all_metrics", "curl http://localhost:8080/actuator/metrics",
            "specific_metric", "curl http://localhost:8080/actuator/metrics/jvm.memory.used",
            "app_info", "curl http://localhost:8080/actuator/info"
        ));
        
        return ResponseEntity.ok(response);
    }
}
