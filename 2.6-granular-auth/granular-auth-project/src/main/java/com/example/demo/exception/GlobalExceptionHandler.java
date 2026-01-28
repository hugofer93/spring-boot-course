package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones.
 * 
 * Centraliza el manejo de todas las excepciones de la aplicación
 * y devuelve respuestas HTTP consistentes con formato JSON.
 * 
 * IMPORTANTE: El orden de los @ExceptionHandler importa.
 * Los manejadores más específicos deben estar antes que los genéricos.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de campos (Bean Validation).
     * 
     * Se activa cuando un DTO con @Valid tiene campos que no cumplen
     * las validaciones (@NotBlank, @Email, @Size, etc.)
     * 
     * Este manejador se ejecuta cuando:
     * - Se envía {} (JSON vacío) → campos son null → @NotBlank falla
     * - Se envía {"username": ""} → campo vacío → @NotBlank falla
     * - Se envía {"username": null} → campo null → @NotBlank falla
     * - Cualquier otra validación falla
     * 
     * Este manejador debe estar ANTES de HttpMessageNotReadableException
     * para que los errores de validación se capturen correctamente.
     * 
     * @param ex Excepción de validación
     * @return Respuesta HTTP 400 con los errores de cada campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation error");
        response.put("message", "One or more fields have validation errors");
        response.put("details", errors);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja errores cuando el cuerpo de la petición está vacío o es inválido.
     * 
     * Se activa cuando:
     * - El body está completamente vacío (sin JSON) → muestra errores de campos faltantes
     * - El JSON es inválido sintácticamente → muestra error de formato
     * 
     * NOTA: Si se envía {} (JSON vacío pero válido), se deserializa correctamente
     * y la validación de campos se maneja en MethodArgumentNotValidException (arriba)
     * 
     * @param ex Excepción de lectura del mensaje
     * @return Respuesta HTTP 400 con errores de validación (si body vacío) o error de formato
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // Si el body está completamente vacío, devolver errores de validación
        // para que sea consistente con el caso de {}
        if (ex.getMessage() != null && ex.getMessage().contains("Required request body is missing")) {
            Map<String, String> errors = new HashMap<>();
            errors.put("username", "Username is required");
            errors.put("password", "Password is required");
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("error", "Validation error");
            response.put("message", "One or more fields have validation errors");
            response.put("details", errors);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(response);
        }
        
        // Para otros casos (JSON inválido sintácticamente)
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Invalid request body");
        
        String message = "Request body must be valid JSON";
        if (ex.getMessage() != null && (ex.getMessage().contains("JSON parse error") || ex.getMessage().contains("Unexpected end-of-input"))) {
            message = "Invalid JSON format";
        }
        
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja errores cuando se usa un método HTTP no soportado.
     * 
     * Se activa cuando:
     * - Se hace GET a un endpoint que solo acepta POST
     * - Se hace POST a un endpoint que solo acepta GET
     * - Cualquier método HTTP no permitido
     * 
     * @param ex Excepción de método no soportado
     * @return Respuesta HTTP 405 (Method Not Allowed) con el mensaje de error
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        response.put("error", "Method not allowed");
        
        String supportedMethods = String.join(", ", ex.getSupportedMethods() != null ? ex.getSupportedMethods() : new String[0]);
        String message = String.format("Method '%s' is not supported. Supported methods: %s", 
                ex.getMethod(), supportedMethods);
        
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * Maneja errores de autenticación de Spring Security.
     * 
     * Se activa cuando las credenciales son incorrectas o el usuario
     * no está habilitado, etc.
     * 
     * @param ex Excepción de autenticación
     * @return Respuesta HTTP 401 (Unauthorized) con el mensaje de error
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("error", "Authentication failed");
        response.put("message", "Invalid username or password");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Maneja excepciones generales no capturadas.
     * 
     * Útil para debugging y para asegurar que siempre se devuelva
     * una respuesta JSON consistente.
     * 
     * @param ex Excepción no manejada
     * @return Respuesta HTTP 500 con el mensaje de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal server error");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
