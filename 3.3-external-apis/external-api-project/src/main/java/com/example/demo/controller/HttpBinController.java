package com.example.demo.controller;

import com.example.demo.dto.HttpBinResponseDTO;
import com.example.demo.exception.ExternalApiException;
import com.example.demo.service.HttpBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para demostrar consumo de httpbin.org API.
 * 
 * httpbin.org es útil para probar diferentes aspectos de peticiones HTTP.
 */
@RestController
@RequestMapping("/api/httpbin")
@RequiredArgsConstructor
public class HttpBinController {

    private final HttpBinService httpBinService;

    // ========== ENDPOINTS CON RESTTEMPLATE ==========

    /**
     * GET /api/httpbin/resttemplate/get
     * 
     * Ejemplo simple de GET request usando RestTemplate.
     */
    @GetMapping("/resttemplate/get")
    public ResponseEntity<HttpBinResponseDTO> getRequestRestTemplate() {
        HttpBinResponseDTO response = httpBinService.getRequestRestTemplate();
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/httpbin/resttemplate/post
     * 
     * Ejemplo de POST request con body JSON usando RestTemplate.
     */
    @PostMapping("/resttemplate/post")
    public ResponseEntity<HttpBinResponseDTO> postRequestRestTemplate(@RequestBody Map<String, Object> data) {
        HttpBinResponseDTO response = httpBinService.postRequestRestTemplate(data);
        return ResponseEntity.ok(response);
    }

    // ========== ENDPOINTS CON WEBCLIENT ==========

    /**
     * GET /api/httpbin/webclient/get
     * 
     * Ejemplo simple de GET request usando WebClient.
     */
    @GetMapping("/webclient/get")
    public Mono<ResponseEntity<HttpBinResponseDTO>> getRequestWebClient() {
        return httpBinService
                .getRequestWebClient()
                .map(ResponseEntity::ok)
                .onErrorResume(ExternalApiException.class, ex -> 
                    Mono.error(ex)); // Propagar la excepción para que GlobalExceptionHandler la maneje
    }

    /**
     * POST /api/httpbin/webclient/post
     * 
     * Ejemplo de POST request con body JSON usando WebClient.
     */
    @PostMapping("/webclient/post")
    public Mono<ResponseEntity<HttpBinResponseDTO>> postRequestWebClient(@RequestBody Map<String, Object> data) {
        return httpBinService
                .postRequestWebClient(data)
                .map(ResponseEntity::ok)
                .onErrorResume(ExternalApiException.class, ex -> 
                    Mono.error(ex)); // Propagar la excepción para que GlobalExceptionHandler la maneje
    }

    /**
     * GET /api/httpbin/webclient/get-with-params?param1=value1&param2=value2
     * 
     * Ejemplo avanzado: GET request con query parameters usando WebClient.
     */
    @GetMapping("/webclient/get-with-params")
    public Mono<ResponseEntity<HttpBinResponseDTO>> getRequestWithParamsWebClient(
            @RequestParam String param1,
            @RequestParam String param2) {
        return httpBinService
                .getRequestWithParamsWebClient(param1, param2)
                .map(ResponseEntity::ok)
                .onErrorResume(ExternalApiException.class, ex -> 
                    Mono.error(ex)); // Propagar la excepción para que GlobalExceptionHandler la maneje
    }

    /**
     * GET /api/httpbin/webclient/get-with-headers
     * 
     * Ejemplo avanzado: GET request con headers personalizados usando WebClient.
     */
    @GetMapping("/webclient/get-with-headers")
    public Mono<ResponseEntity<HttpBinResponseDTO>> getRequestWithHeadersWebClient(
            @RequestHeader("X-Custom-Header") String customHeader) {
        return httpBinService
                .getRequestWithHeadersWebClient(customHeader)
                .map(ResponseEntity::ok)
                .onErrorResume(ExternalApiException.class, ex -> 
                    Mono.error(ex)); // Propagar la excepción para que GlobalExceptionHandler la maneje
    }
}
