package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador raíz para información de la API.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Consumo de APIs Externas - Spring Boot");
        response.put("version", "1.0.0");
        response.put("description", "Ejemplos de consumo de APIs externas usando RestTemplate y WebClient");
        
        // Endpoints de JSONPlaceholder con RestTemplate
        Map<String, String> jsonPlaceholderRestTemplate = new LinkedHashMap<>();
        jsonPlaceholderRestTemplate.put("GET /api/jsonplaceholder/resttemplate/posts/{id}", "Obtener post por ID");
        jsonPlaceholderRestTemplate.put("GET /api/jsonplaceholder/resttemplate/posts", "Listar todos los posts");
        jsonPlaceholderRestTemplate.put("POST /api/jsonplaceholder/resttemplate/posts", "Crear post");
        jsonPlaceholderRestTemplate.put("GET /api/jsonplaceholder/resttemplate/users/{id}", "Obtener usuario por ID");
        
        // Endpoints de JSONPlaceholder con WebClient
        Map<String, String> jsonPlaceholderWebClient = new LinkedHashMap<>();
        jsonPlaceholderWebClient.put("GET /api/jsonplaceholder/webclient/posts/{id}", "Obtener post por ID");
        jsonPlaceholderWebClient.put("GET /api/jsonplaceholder/webclient/posts", "Listar todos los posts");
        jsonPlaceholderWebClient.put("POST /api/jsonplaceholder/webclient/posts", "Crear post");
        jsonPlaceholderWebClient.put("GET /api/jsonplaceholder/webclient/users/{id}", "Obtener usuario por ID");
        jsonPlaceholderWebClient.put("GET /api/jsonplaceholder/webclient/posts/batch?ids=1,2,3", "Obtener múltiples posts en paralelo");
        
        // Endpoints de httpbin con RestTemplate
        Map<String, String> httpBinRestTemplate = new LinkedHashMap<>();
        httpBinRestTemplate.put("GET /api/httpbin/resttemplate/get", "GET request simple");
        httpBinRestTemplate.put("POST /api/httpbin/resttemplate/post", "POST request con body");
        
        // Endpoints de httpbin con WebClient
        Map<String, String> httpBinWebClient = new LinkedHashMap<>();
        httpBinWebClient.put("GET /api/httpbin/webclient/get", "GET request simple");
        httpBinWebClient.put("POST /api/httpbin/webclient/post", "POST request con body");
        httpBinWebClient.put("GET /api/httpbin/webclient/get-with-params?param1=v1&param2=v2", "GET con query parameters");
        httpBinWebClient.put("GET /api/httpbin/webclient/get-with-headers", "GET con headers personalizados");
        
        // Agrupar todos los endpoints
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("jsonplaceholder-resttemplate", jsonPlaceholderRestTemplate);
        endpoints.put("jsonplaceholder-webclient", jsonPlaceholderWebClient);
        endpoints.put("httpbin-resttemplate", httpBinRestTemplate);
        endpoints.put("httpbin-webclient", httpBinWebClient);
        
        response.put("endpoints", endpoints);
        response.put("examples", Map.of(
                "resttemplate", "Cliente HTTP bloqueante (tradicional)",
                "webclient", "Cliente HTTP reactivo (moderno, recomendado)"
        ));
        response.put("apis-externas", Map.of(
                "jsonplaceholder", "https://jsonplaceholder.typicode.com",
                "httpbin", "https://httpbin.org"
        ));
        
        return ResponseEntity.ok(response);
    }
}
