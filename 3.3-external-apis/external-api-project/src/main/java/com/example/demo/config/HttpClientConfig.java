package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuración de clientes HTTP para consumo de APIs externas.
 * 
 * Este archivo configura tanto RestTemplate (bloqueante) como WebClient (reactivo).
 */
@Slf4j
@Configuration
public class HttpClientConfig {

    @Value("${http.client.timeout.connect:5000}")
    private int connectTimeout;

    @Value("${http.client.timeout.read:10000}")
    private int readTimeout;

    @Value("${http.client.max-connections:100}")
    private int maxConnections;

    @Value("${http.client.max-per-route:20}")
    private int maxPerRoute;

    /**
     * Configuración de RestTemplate (bloqueante, tradicional).
     * 
     * RestTemplate es útil para:
     * - Aplicaciones síncronas simples
     * - Migraciones de código legacy
     * - Casos donde no necesitas reactividad
     * 
     * Desventajas:
     * - Bloquea el hilo durante la petición
     * - Menos eficiente para múltiples peticiones concurrentes
     */
    @Bean
    public RestTemplate restTemplate() {
        // Configurar factory con timeouts
        // SimpleClientHttpRequestFactory es más simple y no requiere dependencias adicionales
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        // Crear RestTemplate con factory configurada
        RestTemplate restTemplate = new RestTemplate(factory);

        // Agregar interceptor para logging
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            log.info("RestTemplate Request: {} {}", request.getMethod(), request.getURI());
            long startTime = System.currentTimeMillis();
            try {
                var response = execution.execute(request, body);
                long duration = System.currentTimeMillis() - startTime;
                log.info("RestTemplate Response: {} {} - Duration: {}ms", 
                    response.getStatusCode(), request.getURI(), duration);
                return response;
            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                log.error("RestTemplate Error: {} - Duration: {}ms", request.getURI(), duration, e);
                throw e;
            }
        });
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    /**
     * Configuración de WebClient (reactivo, no bloqueante).
     * 
     * WebClient es la solución moderna recomendada para Spring Boot:
     * - No bloquea hilos (reactivo)
     * - Mejor rendimiento para múltiples peticiones concurrentes
     * - Soporte nativo para programación reactiva (Mono/Flux)
     * - Mejor manejo de errores
     * 
     * Ventajas:
     * - Escalable y eficiente
     * - Integración con Spring WebFlux
     * - Mejor para microservicios
     */
    @Bean
    public WebClient webClient() {
        // Configurar HttpClient de Netty con timeouts
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(readTimeout))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
        
        // Crear WebClient con ReactorClientHttpConnector
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                // Agregar filtro para logging
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    /**
     * Filtro para registrar peticiones HTTP con WebClient.
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("WebClient Request: {} {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    /**
     * Filtro para registrar respuestas HTTP con WebClient.
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("WebClient Response: {} {}", 
                clientResponse.statusCode(), clientResponse.request().getURI());
            return Mono.just(clientResponse);
        });
    }
}
