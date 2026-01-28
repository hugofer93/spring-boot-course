package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador principal que demuestra diferentes niveles de logging.
 */
@RestController
public class HomeController {

    // Logger estático final: mejor práctica para logging
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        logger.info("Solicitud recibida en endpoint raíz");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Logging y Monitoreo");
        response.put("timestamp", LocalDateTime.now());
        response.put("endpoints", Map.of(
            "logging", Map.of(
                "info", "/api/info",
                "debug", "/api/debug",
                "warn", "/api/warn",
                "error", "/api/error",
                "example", "/api/example"
            ),
            "monitoring", Map.of(
                "health", "/actuator/health",
                "metrics", "/actuator/metrics",
                "info", "/actuator/info",
                "monitoring_info", "/api/monitoring"
            )
        ));
        
        logger.debug("Respuesta generada: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/info")
    public ResponseEntity<Map<String, String>> info() {
        // Nivel INFO: información general del flujo de la aplicación
        logger.info("Endpoint /api/info llamado");
        logger.info("Este es un mensaje de nivel INFO - información importante del proceso");
        
        return ResponseEntity.ok(Map.of(
            "level", "INFO",
            "message", "Este endpoint genera logs de nivel INFO",
            "description", "Los logs INFO muestran información importante del flujo de la aplicación"
        ));
    }

    @GetMapping("/api/debug")
    public ResponseEntity<Map<String, String>> debug() {
        // Nivel DEBUG: información detallada para debugging
        logger.debug("Endpoint /api/debug llamado");
        logger.debug("Este es un mensaje de nivel DEBUG - información detallada para desarrollo");
        logger.debug("Parámetros de la petición: ninguno");
        logger.debug("Tiempo de procesamiento: < 1ms");
        
        // También mostrar INFO para que se vea en consola por defecto
        logger.info("Endpoint /api/debug ejecutado (revisa logs DEBUG en archivo)");
        
        return ResponseEntity.ok(Map.of(
            "level", "DEBUG",
            "message", "Este endpoint genera logs de nivel DEBUG",
            "description", "Los logs DEBUG muestran información detallada (solo en desarrollo)",
            "note", "Los logs DEBUG solo aparecen si el nivel está configurado en DEBUG"
        ));
    }

    @GetMapping("/api/warn")
    public ResponseEntity<Map<String, String>> warn() {
        // Nivel WARN: advertencias sobre situaciones inusuales
        logger.warn("Endpoint /api/warn llamado");
        logger.warn("Este es un mensaje de nivel WARN - situación que requiere atención");
        logger.warn("Ejemplo: recurso accedido de forma inusual, pero no es un error");
        
        return ResponseEntity.ok(Map.of(
            "level", "WARN",
            "message", "Este endpoint genera logs de nivel WARN",
            "description", "Los logs WARN indican situaciones inusuales que requieren atención"
        ));
    }

    @GetMapping("/api/error")
    public ResponseEntity<Map<String, String>> error() {
        // Nivel ERROR: errores que no detienen la aplicación
        logger.error("Endpoint /api/error llamado");
        logger.error("Este es un mensaje de nivel ERROR - error que requiere atención inmediata");
        
        // Ejemplo de error con excepción
        try {
            throw new RuntimeException("Ejemplo de excepción para logging");
        } catch (RuntimeException e) {
            logger.error("Error capturado en endpoint /api/error", e);
        }
        
        return ResponseEntity.ok(Map.of(
            "level", "ERROR",
            "message", "Este endpoint genera logs de nivel ERROR",
            "description", "Los logs ERROR indican problemas que requieren atención inmediata"
        ));
    }
}
