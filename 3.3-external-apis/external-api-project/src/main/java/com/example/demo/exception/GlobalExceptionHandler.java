package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para APIs externas.
 * 
 * Captura y maneja diferentes tipos de errores que pueden ocurrir
 * al consumir APIs externas.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones personalizadas de APIs externas.
     */
    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<Map<String, Object>> handleExternalApiException(ExternalApiException ex) {
        log.error("Error al consumir API externa: {}", ex.getMessage(), ex);
        
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", ex.getStatusCode() > 0 ? ex.getStatusCode() : 500);
        error.put("error", "Error al consumir API externa");
        error.put("message", ex.getMessage());
        error.put("api", ex.getApiName());
        
        HttpStatus status = ex.getStatusCode() > 0 
            ? HttpStatus.valueOf(ex.getStatusCode()) 
            : HttpStatus.INTERNAL_SERVER_ERROR;
        
        return ResponseEntity.status(status).body(error);
    }

    /**
     * Maneja errores HTTP 4xx de RestTemplate.
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.error("Error HTTP del cliente (4xx): {}", ex.getMessage(), ex);
        
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", ex.getStatusCode().value());
        error.put("error", "Error del cliente HTTP");
        error.put("message", ex.getMessage());
        
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    /**
     * Maneja errores HTTP 5xx de RestTemplate.
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpServerErrorException(HttpServerErrorException ex) {
        log.error("Error HTTP del servidor (5xx): {}", ex.getMessage(), ex);
        
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", ex.getStatusCode().value());
        error.put("error", "Error del servidor HTTP");
        error.put("message", "El servidor externo respondió con un error");
        
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    /**
     * Maneja errores de conexión/timeout de RestTemplate.
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAccessException(ResourceAccessException ex) {
        log.error("Error de acceso al recurso (timeout/conexión): {}", ex.getMessage(), ex);
        
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.GATEWAY_TIMEOUT.value());
        error.put("error", "Error de conexión o timeout");
        error.put("message", "No se pudo conectar con la API externa o se agotó el tiempo de espera");
        
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(error);
    }

    /**
     * Maneja errores de WebClient (reactivo).
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("Error de respuesta de WebClient: {}", ex.getMessage(), ex);
        
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", ex.getStatusCode().value());
        error.put("error", "Error de respuesta de API externa");
        error.put("message", ex.getMessage());
        
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    /**
     * Maneja otros errores de WebClient.
     */
    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<Map<String, Object>> handleWebClientException(WebClientException ex) {
        log.error("Error de WebClient: {}", ex.getMessage(), ex);
        
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Error al consumir API externa");
        error.put("message", "Ocurrió un error inesperado al comunicarse con la API externa");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Maneja cualquier otra excepción no prevista.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Error interno del servidor");
        error.put("message", "Ocurrió un error inesperado");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
