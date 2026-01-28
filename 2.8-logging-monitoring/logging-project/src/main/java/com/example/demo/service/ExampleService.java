package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Servicio de ejemplo que demuestra logging en la capa de servicio.
 * 
 * Muestra cómo usar logging para:
 * - Entrada y salida de métodos
 * - Validaciones y reglas de negocio
 * - Manejo de errores
 */
@Service
public class ExampleService {

    private static final Logger logger = LoggerFactory.getLogger(ExampleService.class);

    /**
     * Método que procesa datos con logging detallado.
     */
    public String processData(String input) {
        logger.info("Iniciando procesamiento de datos. Input: {}", input);
        
        if (input == null || input.isBlank()) {
            logger.warn("Input vacío o nulo recibido en processData");
            return "Input inválido";
        }

        logger.debug("Validación de input exitosa. Longitud: {}", input.length());

        try {
            // Simular procesamiento
            String result = input.toUpperCase();
            logger.debug("Procesamiento completado. Resultado: {}", result);
            logger.info("Procesamiento exitoso. Input procesado correctamente");
            
            return result;
        } catch (Exception e) {
            logger.error("Error durante el procesamiento de datos", e);
            throw new RuntimeException("Error al procesar datos", e);
        }
    }

    /**
     * Método que simula una operación costosa con logging de rendimiento.
     */
    public void performExpensiveOperation() {
        logger.info("Iniciando operación costosa");
        long startTime = System.currentTimeMillis();

        try {
            // Simular operación costosa
            Thread.sleep(100);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Operación costosa completada en {} ms", duration);
            
            if (duration > 200) {
                logger.warn("Operación costosa tardó más de lo esperado: {} ms", duration);
            }
        } catch (InterruptedException e) {
            logger.error("Operación costosa interrumpida", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Método que demuestra logging condicional.
     */
    public void conditionalLogging(boolean condition) {
        if (logger.isDebugEnabled()) {
            logger.debug("Logging condicional activado. Condición: {}", condition);
        }

        if (condition) {
            logger.info("Condición verdadera - ejecutando lógica principal");
        } else {
            logger.warn("Condición falsa - ejecutando lógica alternativa");
        }
    }
}
