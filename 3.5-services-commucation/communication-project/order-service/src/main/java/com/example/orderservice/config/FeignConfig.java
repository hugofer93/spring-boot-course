package com.example.orderservice.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración personalizada para Feign Client
 * 
 * Esta clase permite personalizar el comportamiento de Feign,
 * como agregar headers, configurar logging, manejar errores, etc.
 */
@Slf4j
@Configuration
public class FeignConfig {

    /**
     * Configura el nivel de logging de Feign
     * 
     * Niveles disponibles:
     * - NONE: No logging (por defecto)
     * - BASIC: Solo request method y URL, response status y tiempo de ejecución
     * - HEADERS: BASIC + request y response headers
     * - FULL: HEADERS + request y response body
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // Cambiar a BASIC o HEADERS en producción
    }

    /**
     * Interceptor para agregar headers personalizados a todas las peticiones Feign
     * 
     * Útil para agregar:
     * - Tokens de autenticación
     * - Headers de trazabilidad (correlation IDs)
     * - Headers de versión de API
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Ejemplo: Agregar un header de correlación
                template.header("X-Request-Id", java.util.UUID.randomUUID().toString());
                
                // Ejemplo: Agregar header de origen
                template.header("X-Service-Name", "order-service");
                
                log.debug("Agregando headers personalizados a la petición: {}", template.url());
            }
        };
    }
}
