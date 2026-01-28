package com.example.demo.service;

import com.example.demo.dto.HttpBinResponseDTO;
import com.example.demo.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio de ejemplo para consumir httpbin.org API.
 * 
 * httpbin.org es una API de prueba útil para:
 * - Probar diferentes métodos HTTP
 * - Ver headers enviados
 * - Probar autenticación
 * - Simular delays y errores
 * 
 * https://httpbin.org
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HttpBinService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Value("${external.apis.httpbin.base-url}")
    private String baseUrl;

    /**
     * Ejemplo con RestTemplate: GET request simple.
     */
    public HttpBinResponseDTO getRequestRestTemplate() {
        try {
            String url = baseUrl + "/get";
            log.info("Haciendo GET request a httpbin con RestTemplate");
            
            var response = restTemplate.getForEntity(url, HttpBinResponseDTO.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("GET request exitoso. URL recibida: {}", response.getBody().getUrl());
                return response.getBody();
            }
            
            throw new ExternalApiException("HttpBin", 
                "No se pudo obtener la respuesta", HttpStatus.INTERNAL_SERVER_ERROR.value());
                
        } catch (Exception e) {
            log.error("Error al hacer GET request: {}", e.getMessage());
            throw new ExternalApiException("HttpBin", 
                "Error al hacer GET request", 0);
        }
    }

    /**
     * Ejemplo con RestTemplate: POST request con body JSON.
     */
    public HttpBinResponseDTO postRequestRestTemplate(Map<String, Object> data) {
        try {
            String url = baseUrl + "/post";
            log.info("Haciendo POST request a httpbin con RestTemplate");
            
            var response = restTemplate.postForEntity(url, data, HttpBinResponseDTO.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("POST request exitoso");
                return response.getBody();
            }
            
            throw new ExternalApiException("HttpBin", 
                "No se pudo hacer el POST request", HttpStatus.INTERNAL_SERVER_ERROR.value());
                
        } catch (Exception e) {
            log.error("Error al hacer POST request: {}", e.getMessage());
            throw new ExternalApiException("HttpBin", 
                "Error al hacer POST request", 0);
        }
    }

    /**
     * Ejemplo con WebClient: GET request simple.
     */
    public Mono<HttpBinResponseDTO> getRequestWebClient() {
        log.info("Haciendo GET request a httpbin con WebClient");
        
        return webClient
                .get()
                .uri(baseUrl + "/get")
                .retrieve()
                .bodyToMono(HttpBinResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("GET request exitoso. URL recibida: {}", response.getUrl()))
                .doOnError(error -> log.error("Error al hacer GET request: {}", error.getMessage()))
                .onErrorMap(ex -> new ExternalApiException("HttpBin", 
                    "Error al hacer GET request", 0));
    }

    /**
     * Ejemplo con WebClient: POST request con body JSON.
     */
    public Mono<HttpBinResponseDTO> postRequestWebClient(Map<String, Object> data) {
        log.info("Haciendo POST request a httpbin con WebClient");
        
        return webClient
                .post()
                .uri(baseUrl + "/post")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(HttpBinResponseDTO.class)
                .doOnSuccess(response -> log.info("POST request exitoso"))
                .doOnError(error -> log.error("Error al hacer POST request: {}", error.getMessage()))
                .onErrorMap(ex -> new ExternalApiException("HttpBin", 
                    "Error al hacer POST request", 0));
    }

    /**
     * Ejemplo avanzado: GET request con query parameters usando WebClient.
     */
    public Mono<HttpBinResponseDTO> getRequestWithParamsWebClient(String param1, String param2) {
        log.info("Haciendo GET request con parámetros a httpbin con WebClient");
        
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/get")
                    .queryParam("param1", param1)
                    .queryParam("param2", param2)
                    .build())
                .retrieve()
                .bodyToMono(HttpBinResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("GET request con parámetros exitoso"))
                .doOnError(error -> log.error("Error al hacer GET request con parámetros: {}", error.getMessage()))
                .onErrorMap(ex -> new ExternalApiException("HttpBin", 
                    "Error al hacer GET request con parámetros", 0));
    }

    /**
     * Ejemplo avanzado: GET request con headers personalizados usando WebClient.
     */
    public Mono<HttpBinResponseDTO> getRequestWithHeadersWebClient(String customHeader) {
        log.info("Haciendo GET request con headers personalizados a httpbin con WebClient");
        
        return webClient
                .get()
                .uri(baseUrl + "/get")
                .header("X-Custom-Header", customHeader)
                .header("X-Request-ID", java.util.UUID.randomUUID().toString())
                .retrieve()
                .bodyToMono(HttpBinResponseDTO.class)
                .doOnSuccess(response -> {
                    log.info("GET request con headers exitoso");
                    log.debug("Headers recibidos: {}", response.getHeaders());
                })
                .doOnError(error -> log.error("Error al hacer GET request con headers: {}", error.getMessage()))
                .onErrorMap(ex -> new ExternalApiException("HttpBin", 
                    "Error al hacer GET request con headers", 0));
    }
}
